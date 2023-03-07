# base image
FROM openjdk:8-jdk-alpine
# copy arthas
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
ENV WORKDIR=/work
EXPOSE 8080
VOLUME WORKDIR
ARG JAVA_OPTS='-server -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$WORKDIR/logs/gc_%p.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=30m -XX:+HeapDumpOnOutOfMemoryError'
ENV PROJECT sdeven-cloud-single-app-1
ADD ./${PROJECT}-*.jar $WORKDIR/
ENV MEM_OPTS="-Xms1024m -Xmx1024m"
WORKDIR $WORKDIR
CMD java ${JAVA_OPTS} ${MEM_OPTS} -Duser.timezone=GMT+8 -jar ${PROJECT}-*.jar
