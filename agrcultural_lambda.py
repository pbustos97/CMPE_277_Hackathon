import json
import pandas as pd
import numpy as np
import boto3
import logging

logger = logging.getLogger()
logger.setLevel(logging.DEBUG)

def lambda_handler(event, context):
    logger.debug("[DEBUG] event: {}".format(event))
    try:
        s3 = boto3.client('s3') 
        obj = s3.get_object(Bucket="hackthon272", Key=f"agricultual.csv")

        agriclture_table = pd.read_csv(obj['Body'])
        logger.debug("[DEBUG] agriclture_table: {}".format(agriclture_table))
        
        country = event["queryStringParameters"]["country"]
        logger.debug("[DEBUG] country: {}".format(country))
        if country.lower() == "United States".lower():
            country = "USA"
        if country.lower() == "India".lower():
            country = "IND"
        if country.lower() == "China".lower():
            country = "CHN"
        logger.debug("[DEBUG] country: {}".format(country))
        agriclture_table_country = agriclture_table.loc[agriclture_table["Country Code"] == country]
        agriclture_table_indicator = agriclture_table_country.loc[agriclture_table["Indicator Name"] == event["queryStringParameters"]["indicator"]]
        agriclture_table_only_year = agriclture_table_indicator.drop(['Country Name','Country Code','Indicator Name','Indicator Code'], axis=1)
        output = []
        for i in agriclture_table_only_year.columns:
            output.append([float(i), agriclture_table_only_year[i].values[0]])
        
        # convert NaN to 
        new_output= np.where(np.isnan(output), 0, output)
        data = {
            'data' : new_output.tolist()
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
