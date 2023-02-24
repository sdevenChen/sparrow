package com.sdeven.sparrow.starter.nodeflow;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sdeven.sparrow.starter.nodeflow.groovy.ExprSupport;
import com.sdeven.sparrow.starter.nodeflow.spi.NfComponent;
import com.sdeven.sparrow.starter.nodeflow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @Description com.sdeven.sparrow.starter.nodeflow 执行引擎
 * @Author sdeven.chen.dongwei@gmail.com
 * @Create 12/12/20 10:09
 */
@Slf4j
public class Nodeflow {
    private static final String EMPTY_STRING = "";
    private static final Map<String, NfComponent> INIT_COMPONENTS = new HashMap<>(1);
    private static final Map<String, NfComponent> COMPONENTS = new HashMap<>(1);

    static {
        ServiceLoader<NfComponent> services = ServiceLoader.load(NfComponent.class);
        for (NfComponent service : services) {
            INIT_COMPONENTS.put(service.spiId(), service);
        }
    }

    private JsonObject nf = null;
    //输入输出
    private JsonArray indexs = null;
    //节点集
    private JsonArray nodes = null;
    private Map<String, Object> ctx;
    //自定义开始执行节点
    private String curNode = null;
    //输出
    private Map<String, Object> output;
    //输入
    private Map<String, Object> input;

    public Nodeflow(String nfjson) {
        Gson gson = new Gson();
        nf = gson.fromJson(nfjson, JsonObject.class);
        log.debug(nf.toString());
        indexs = nf.getAsJsonArray(NodeflowResolveField.KEY_INDEXS);
        nodes = nf.getAsJsonArray(NodeflowResolveField.KEY_NODES);
    }

    /**
     * @Description  注册组件
     * @Date 12/12/20 14:12
     * @Param NfComponent nfc
     * @return void
     */
    public static void register(NfComponent nfc) {
        if (componentExists(nfc)) {
            log.warn("The commponent[{}] register duplicated:{}", nfc.spiId(), nfc.getClass().getName());
        }
        COMPONENTS.put(nfc.spiId(), nfc);
    }
    /**
     * @Description  判断组件是否存在
     * @Date 12/12/20 14:12
     * @Param
     * @return
     */
    public static boolean componentExists(NfComponent c) {
        return COMPONENTS.containsKey(c.spiId()) || INIT_COMPONENTS.containsKey(c.spiId());
    }

    /**
     * @Description  通过spiId 查询组件
     * @Date 12/12/20 14:11
     * @Param
     * @return
     */
    public static NfComponent findComponent(String spiId) {
        NfComponent nfc = COMPONENTS.get(spiId);
        if (nfc == null) {
            nfc = INIT_COMPONENTS.get(spiId);
        }
        return nfc;
    }

    public void execute() throws NodeflowException {
        execute(new HashMap<>(1));
    }

    /**
     * @Description  执行脚本，输入参数
     * @Date 12/12/20 14:12
     * @Param
     * @return
     */
    public void execute(Map<String, Object> ctx) throws NodeflowException {
        this.ctx = ctx;
        List<Map<String, Object>> list = fetchInputOutput(ctx);
        input = list.get(0);
        output = list.get(1);
        if (curNode == null || EMPTY_STRING.equals(curNode.trim())) {
            next(nodes.get(0).getAsJsonObject());
        } else {
            next(findNode(curNode, nodes));
        }
    }
    /**
     * @Description  获取输出
     * @Date 12/12/20 14:14
     * @Param void
     * @return Map<String, Object>
     */
    public Map<String, Object> getOutput() {
        if (output != null) {
            for (String key : output.keySet()) {
                output.put(key, ctx.get(key));
            }
        }
        return output;
    }

    /**
     * @Description  获取输入
     * @Date 12/12/20 14:14
     * @Param void
     * @return Map<String, Object>
     */
    public Map<String, Object> getInput() {
        return this.input;
    }

    private static int getAsInt(JsonObject obj, String key, int def) {
        JsonElement item = obj.get(key);
        return item == null || item.isJsonNull() ? def : item.getAsInt();
    }

    private static String getAsString(JsonObject obj, String key, String def) {
        JsonElement item = obj.get(key);
        return item == null || item.isJsonNull() ? def : item.getAsString();
    }
    /**
     * @Description  输入类型验证
     * @Date 12/12/20 14:15
     * @Param String type, Object val
     * @return Object
     */
    private static Object parseType(String type, Object val) {
        switch (type.toUpperCase()) {
            case NodeflowSupportFieldType.KEY_TYPE_INT:
                val = !StringUtils.isNotEmptyOrNvlObj(val) ? 0 : Integer.parseInt(val.toString().trim());
                break;
            case NodeflowSupportFieldType.KEY_TYPE_LONG:
                val = !StringUtils.isNotEmptyOrNvlObj(val) ? 0 : Long.parseLong(val.toString().trim());
                break;
            case NodeflowSupportFieldType.KEY_TYPE_DOUBLE:
                val = !StringUtils.isNotEmptyOrNvlObj(val) ? 0 : Double.parseDouble(val.toString().trim());
                break;
            case NodeflowSupportFieldType.KEY_TYPE_NUMERIC:
                val = !StringUtils.isNotEmptyOrNvlObj(val) ? 0 : new BigDecimal(val.toString().trim());
                break;
        }
        return val;
    }
    /**
     * @Description  获取输入输出
     * @Date 12/12/20 14:16
     * @Param Map<String, Object> ctx
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> fetchInputOutput(Map<String, Object> ctx) throws NodeflowException {
        Map<String, Object> input = new HashMap<>(1);
        Map<String, Object> output = new HashMap<>(1);
        for (int i = 0; indexs != null && i < indexs.size(); i++) {
            JsonObject indexItem = indexs.get(i).getAsJsonObject();
            int opt = getAsInt(indexItem, NodeflowResolveField.KEY_OPT, 0);
            int mode = getAsInt(indexItem, NodeflowField.KEY_MODE, 1);
            String idx = indexItem.get(NodeflowField.KEY_IDX).getAsString();
            String idxName = getAsString(indexItem, NodeflowField.KEY_NAME, idx);
            String pattern = getAsString(indexItem, NodeflowField.KEY_REGX, EMPTY_STRING);
            String type = getAsString(indexItem, NodeflowResolveField.KEY_TYPE, null);
            String patternName = getAsString(indexItem, NodeflowField.KEY_REGX_NAME, idxName);
            Object val = ctx.get(idx);
            if (type != null) {
                val = parseType(type, val);
            }
            if ((mode & 1) == 1) {
                if (opt == 0) {
                    if (val == null || EMPTY_STRING.equals(val.toString().trim())) {
                        log.info("输入参数不能为空,idx:{}", idx);
                        throw new IllegalArgumentException(idx + "-" + idxName);
                    }
                }
                if (pattern != null && !pattern.equals(EMPTY_STRING) && val != null && !EMPTY_STRING.equals(val.toString().trim())) {
                    if (!val.toString().matches(pattern)) {
                        log.info("输入参数验证不通过,idx:{}，patternName:{}", idx, patternName);
                        throw new IllegalArgumentException(idx + "-" + patternName);
                    }
                }
                ctx.put(idx, val);
                input.put(idx, val);
            } else if ((mode & 2) == 2) {
                output.put(idx, val);
            }
        }
        List<Map<String, Object>> list = new ArrayList<>(2);
        list.add(input);
        list.add(output);
        return list;
    }
    /**
     * @Description  获取下一个执行节点
     * @Date 12/12/20 14:16
     * @Param  JsonObject node
     * @return void
     */
    private void next(JsonObject node) throws NodeflowException {
        String nodeId = node.get(NodeflowResolveField.KEY_ID).getAsString();
        log.debug("node:{}-{}", this, nodeId);
        if (nodeId != null) {
            curNode = nodeId;
        }
        JsonElement componentId = node.get(NodeflowResolveField.KEY_COMPONENT);
        if (componentId != null) {
            NfComponent cpt = findComponent(componentId.getAsString());
            if (cpt != null) {
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> nodemap = new Gson().fromJson(node, type);
                cpt.execute(nodemap, ctx);
            } else {
                throw new RuntimeException("Component not found componentId:" + componentId);
            }
        }
        JsonElement next = node.get(NodeflowResolveField.KEY_NEXT);
        String to = null;
        if (next != null && next.isJsonPrimitive()) {
            to = next.getAsString();
            next(findNode(to, nodes));
        } else if (next != null && next.isJsonArray()) {
            JsonArray nexts = next.getAsJsonArray();
            for (int i = 0; i < nexts.size(); i++) {
                JsonObject it = nexts.get(i).getAsJsonObject();
                JsonElement cond = it.get(NodeflowResolveField.KEY_CONDITION);
                String condition = null;
                if (cond != null && !EMPTY_STRING.equals((condition = cond.getAsString()).trim())) {
                    if ((Boolean) ExprSupport.parseExpr(condition, ctx)) {
                        to = it.get(NodeflowResolveField.KEY_NEXT).getAsString();
                        next(findNode(to, nodes));
                        break;
                    } else {
                        JsonElement errorEl = it.get(NodeflowResolveField.KEY_ERROR);
                        if (errorEl != null) {
                            throw new NodeflowException(errorEl.getAsString());
                        }
                    }
                } else {
                    to = it.get(NodeflowResolveField.KEY_NEXT).getAsString();
                    next(findNode(to, nodes));
                    break;
                }
            }
        }
    }
    /**
     * @Description  查询节点
     * @Date 12/12/20 14:17
     * @Param String id, JsonArray arr
     * @return JsonObject
     */
    private static JsonObject findNode(String id, JsonArray arr) {
        JsonObject item = null;
        for (int i = 0; i < arr.size(); i++) {
            item = arr.get(i).getAsJsonObject();
            if (id.equals(item.get(NodeflowResolveField.KEY_ID).getAsString())) {
                return item;
            }
        }
        return null;
    }
    /**
     * @Description  获取流程执行过程性参数
     * @Date 12/12/20 14:18
     * @Param
     * @return
     */
    public Map<String, Object> getCtx() {
        return ctx;
    }
    /**
     * @Description  获取自定义开始节点
     * @Date 12/12/20 14:17
     * @Param void
     * @return String
     */
    public String getCurNode() {
        return curNode;
    }
    /**
     * @Description  设置自定义开始节点
     * @Date 12/12/20 14:17
     * @Param void
     * @return String
     */
    public void setCurNode(String curNode) {
        this.curNode = curNode;
    }

    /**
     * @Description  清除组件
     * @Date 12/12/20 14:18
     * @Param void
     * @return void
     */
    public static void clearComponents() {
        COMPONENTS.clear();
    }
}
