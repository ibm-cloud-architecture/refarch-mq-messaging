#!/bin/bash
wsimport -s BrownEJB/ejbModule -d BrownEJB/build/classes http://172.16.251.37:9080/inventory/ws/DALServiceService.wsdl -keep 
