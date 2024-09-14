{
 "nodes":[
   {"id":"n1","next":"n3","component":"test_int_out_component"},
   {"id":"n3","next":"n4","component":"test_int_out_component"},
   {"id":"n4","component":"ruleset","ruleset":["deleteSqlType=true;"],
   "next": [
              {
                "condition": "deleteSqlType",
                "next": "genDeleteSql"
              },
              {
                "next": "concatEndSql"
              }
            ]
   },
    {"id":"genDeleteSql","next":"concatEndSql","component":"gen_delete_component"]},
 ]
}