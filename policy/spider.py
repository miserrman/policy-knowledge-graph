import requests
import json
import csv
import pandas as pd
import codecs


FUJIAN_CITY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList'
FUJIAN_REGION = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList?sitePunid=#&type=1'
FUJIAN_DEPART = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getDeptByAreaBelongto?type=1&deptUnid=#'
FUJIAN_POLICY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getQzqdInfoList?stype=&deptOrSiteunid=#&subname=&pageSize=10&pageNum=1'
FUJIAN_POLICY_CONTENT = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingDetail/getPowerDetail?powerunid=#&type=1'


def get_request(url, params=None):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    }
    response = requests.get(url, params=params, headers=headers)
    content = response.content.decode('utf-8')
    print(content)
    data = json.loads(content)
    return data.get('data')


def write_csv(data):
    with open('data/policy_id.csv', 'a', encoding='utf-8-sig') as fp:
        csv_writer = csv.writer(fp)
        for row in data:
            data_row = [row[key] for key in row.keys()]
            csv_writer.writerow(data_row)
        fp.close()


def create_policy_title_chart():
    """
    构造policy title表
    :return:
    """
    city_list = get_request(FUJIAN_CITY)
    data = []
    for city in city_list:
        pun_id = city.get('sitePunid')
        print(city.get('siteName'))
        region_url = FUJIAN_CITY.replace('#', pun_id)
        region_list = get_request(region_url)
        for region in region_list:
            print(region.get('siteName'))
            dept_id = region.get('deptUnid')
            dept_url = FUJIAN_DEPART.replace('#', dept_id)
            department_list = get_request(dept_url)
            for depart in department_list:
                print(depart.get('name'))
                dept_or_site_unid = depart.get('unid')
                policy_url = FUJIAN_POLICY.replace('#', dept_or_site_unid)
                policy_list = get_request(policy_url)
                for policy in policy_list:
                    if str(city.get('siteName')) != str(region.get('siteName')):
                        continue
                    sub_data = {
                        'city': city.get('siteName'),
                        'region': region.get('siteName'),
                        'depart': depart.get('name'),
                        'policy_id': policy.get('unid'),
                        'policy_title': policy.get('liabilitisename')
                    }
                    data.append(sub_data)
    write_csv(data)


def create_policy_content_chart(policy_chart):
    """
    使用policy id表生成具体的政策内容
    :param policy_chart: 第一张表的文件名
    :return: 生成具体政策表
    """
    col = ['id', 'type', 'department', 'level', 'content']
    new_data = pd.DataFrame(columns=col)
    data = pd.read_csv(policy_chart, names=['city', 'region', 'depart', 'policy_id', 'policy_title'])
    for index, row in data.iterrows():
        print(index)
        content_url = FUJIAN_POLICY_CONTENT.replace("#", str(row['policy_id']))
        policy_content = get_request(content_url)
        if policy_content == None:
            continue
        d_arr = [
            policy_content.get('unid'),
            policy_content.get('stype'),
            policy_content.get('executor'),
            policy_content.get('xslevel'),
            policy_content.get('according')
        ]
        s = pd.Series(d_arr, index=col)
        new_data = new_data.append(s, ignore_index=True)
    new_data.to_csv('data/policy_content.csv', index=False)


if __name__ == '__main__':
    create_policy_content_chart('data/policy_id.csv')