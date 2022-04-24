import json
import pandas as pd
import boto3
import logging

logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

def lambda_handler(event, context):
    logger.debug("[DEBUG] event: {}".format(event))
    try:
        s3 = boto3.client('s3') 
        obj = s3.get_object(Bucket="hackthon272", Key="countryfinancestable - Sheet1.csv") 
        
        # logger.debug("[DEBUG] body: {}".format(obj))
        # a = obj['Body'].read()
        # logger.debug("[DEBUG] object = {}".format(a))
        
        debt_table = pd.read_csv(obj['Body']) # 'Body' is a key word
        logger.debug("[DEBUG] debt_table: {}".format(debt_table))
        #logger.debug("[DEBUG] debt_table: {}".format(debt_table))
        country = event["queryStringParameters"]["country"]
        logger.debug("[DEBUG] country: {}".format(country))
        if country.lower() == "United States".lower():
            country = "USA"
        if country.lower() == "India".lower():
            country = "IND"
        if country.lower() == "China".lower():
            country = "CHN"
        logger.debug("[DEBUG] country: {}".format(country))
        debt_table_country = debt_table.loc[debt_table["Country Code"] == country]
        debt_table_indicator = debt_table_country.loc[debt_table["Indicator Name"] == event["queryStringParameters"]["indicator"]]
        debt_table_only_year = debt_table_indicator.drop(['Country Name','Country Code','Indicator Name','Indicator Code'], axis=1)
    
        output = []
        for i in debt_table_only_year.columns:
            output.append([float(i), debt_table_only_year[i].values[0]])
            
            
        data = {
            'data': output
        }
        logger.debug("[DEBUG] data: {}".format(data))
        return {
            'statusCode': 200,
            'headers': {
                "Access-Control-Allow-Headers" : "Content-Type",
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods": "*",
                'Access-Control-Allow-Credentials': True,
                'Content-Type': 'application/json'
            },
            'body': json.dumps(data)
        }
    except Exception as e: 
        return {
            'statusCode': 500,
            'body': json.dumps(e),
            'isBase64Encoded': False,
            'headers': {
                "Access-Control-Allow-Headers" : "Content-Type",
                "Access-Control-Allow-Origin": "*",
                "Access-Control-Allow-Methods": "*",
                'Access-Control-Allow-Credentials': True,
                'Content-Type': 'application/json'
            }
        }
        

