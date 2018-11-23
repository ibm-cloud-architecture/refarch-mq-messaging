#!/bin/bash
wsimport -s ejbModule -d build/classes http:///inventory/ws?wsdl -keep 
