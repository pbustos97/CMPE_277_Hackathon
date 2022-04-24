import json
import logging
import boto3
import os

logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

def lambda_handler(event, context):
    logger.debug("[DEBUG] event type: {}".format(type(event)))
    logger.debug("[DEBUG] event keys: {}".format(event.keys()))
    if type(event) == str:
        event = json.loads(event)
    logger.debug("[DEBUG] event: {}".format(event))
    queries = event['queryStringParameters']
    if type(queries) == str:
        queries = json.loads(queries)
    logger.debug("[DEBUG] queryStringParameters: {}".format(queries))
    country = queries['country']
    indicator = queries['indicator']
    try:
        s3 = boto3.client('s3') 
        obj = None
        obj2 = None
        logger.debug("[DEBUG] indicator: {}".format(type(indicator)))
        if 'gdp' == indicator.lower():
            obj = s3.get_object(Bucket="hackthon272", Key="GDP USD - API_NY.GDP.MKTP.CD_DS2_en_csv_v2_3263806.csv") 
        elif 'inflow' == indicator.lower():
            obj = s3.get_object(Bucket="hackthon272", Key="Foreign direct investment, net inflows (% of GDP) - API_BX.KLT.DINV.WD.GD.ZS_DS2_en_csv_v2_3159100.csv")
        elif 'outflow' in indicator.lower():
            obj = s3.get_object(Bucket="hackthon272", Key="Foreign direct investment, net outflows (BoP, current US$) - BM-1.KLT.DINV.CD.WD.csv")
        elif 'net' == indicator.lower(): 
            obj = s3.get_object(Bucket="hackthon272", Key="Foreign direct investment, net inflows (% of GDP) - API_BX.KLT.DINV.WD.GD.ZS_DS2_en_csv_v2_3159100.csv")
            obj2 = s3.get_object(Bucket="hackthon272", Key="Foreign direct investment, net outflows (BoP, current US$) - BM-1.KLT.DINV.CD.WD.csv")
        else:
            return returnResponse(500, {'message': 'no valid filter'})
        csv = obj['Body'].read()
        logger.debug("[DEBUG] object = {}".format(csv))
        
        a = convertCsvToJson(csv, country)
        logger.debug("[DEBUG] csv1: {}".format(a))
        b = None
        if obj2 != None:
            csv2 = obj2['Body'].read()
            logger.debug("[DEBUG] object2 = {}".format(csv2))
            b = convertCsvToJson(csv2, country)
        res = a
        if b != None:
            for i in range(0, len(res)):
                res[i] = a[i][1] - b[i][1]
        data = {
            'data': res
        }
        return returnResponse(200, data)
        
    except Exception as e:
        logger.debug("[ERROR] {}".format(e))
    return returnResponse(500, {'message': 'error'
    })

def convertCsvToJson(obj, country):
    res = []
    obj = obj.decode("utf-8")
    logger.debug("[DEBUG] convertCsvToJson obj: {}".format(obj))
    logger.debug("[DEBUG] convertCsvToJson obj type: {}".format(type(obj)))
    logger.debug("[DEBUG] convertCsvToJson country: {}".format(country))
    obj = obj.split('\n')
    logger.debug("[DEBUG] convertCsvToJson line 0: {}".format(obj[0]))
    line = obj[0]
    line = line.split(',')
    logger.debug("[DEBUG] convertCsvToJson line 0: {}".format(line))
    countryIndex = 0
    for i in range(0, len(line)):
        if country == line[i] or country in line[i]:
            countryIndex = i
            break
    for i in range(1, len(obj)-1):
        line = obj[i]
        line = line.split(',')
        logger.debug("[DEBUG] convertCsvToJson line: {}".format(line))
        year = float(line[0])
        value = float(line[countryIndex])
        res.append([year, value])
    return res

def returnResponse(statusCode, body):
    logger.debug('[RESPONSE] statusCode: {} body: {}'.format(statusCode, body))
    logger.debug('[RESPONSE] json.dumps(body): {}'.format(json.dumps(body)))
    return {
        'statusCode': statusCode,
        'body': json.dumps(body),
        'isBase64Encoded': False,
        'headers': {
            "Access-Control-Allow-Headers" : "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "*",
            'Access-Control-Allow-Credentials': True,
            'Content-Type': 'application/json'
        }
    }
