#!/bin/bash
wsimport -s ejbModule -d build/classes http://172.16.251.37:9080/inventory/ws/DALServiceService.wsdl -keep 
