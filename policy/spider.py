import requests
import json
import csv
import pandas as pd
import codecs
from lxml import etree
from bs4 import BeautifulSoup
import re


FUJIAN_CITY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList'
FUJIAN_REGION = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList?sitePunid=#&type=1'
FUJIAN_DEPART = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getDeptByAreaBelongto?type=1&deptUnid=#'
FUJIAN_POLICY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getQzqdInfoList?stype=&deptOrSiteunid=#&subname=&pageSize=10&pageNum=1'
FUJIAN_POLICY_CONTENT = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingDetail/getPowerDetail?powerunid=#&type=1'
FUJIAN_UNIFORM_POLICY_NET = 'https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=41&dosubmit=1&subcatid=#&start_time=&end_time=&title='


def get_page(html):
    """
    得到政策网站的页数和每页文件的数量
    :param html:
    :return:
    """
    total_object = re.search(r'total = \"\d+\"', html)
    count_object = re.search(r'count = \d+', html)
    if total_object and count_object:
        total = int(str(re.search('\d+', str(total_object.group())).group()))
        count = int(str(re.search('\d+', str(count_object.group())).group()))
    return total, count


def load_clear_info(path):
    """
    下载政策网站具体的信息
    :param path:
    :return:
    """
    data = pd.read_csv(path, names=['depart_id', 'detail', 'title'])
    headers = {
        'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    }
    for index, row in data.iterrows():
        url = row['detail']
        content = requests.get(url, headers=headers)
        soup = BeautifulSoup(content.text)
        title = soup.findAll(name='div', attrs={'class': 'col-xs-12 page-header'})
        if title:
            header = title[0].h1.string
        data.iloc[index, 2] = header
    data.to_csv(path, index=True)


def load_html(url, params=None):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    }
    department_value = [i for i in range(699, 715)]
    department_value += [731, 741, 925]
    result = []
    for value in department_value:
        url = FUJIAN_UNIFORM_POLICY_NET.replace('#', str(value))
        request = requests.get(url, headers=headers)
        html_text = request.text
        total, limit = get_page(html_text)
        if total == 0:
            page = 0
        else:
            page = int(total / limit) + 1
        for i in range(1, page + 1):
            page_url = url + '&page=' + str(i)
            page_request = requests.get(page_url, headers=headers)
            page_text = page_request.text
            print(page_text)
            soup = BeautifulSoup(page_text)
            links = soup.findAll(name='a', attrs={'class': 'weui-media-box weui-media-box_appmsg show-loading'})
            for link in links:
                href = link['href']
                head = link.find(name='h4')
                title = head.string
                res = {
                    'depart_id': value,
                    'link': href,
                    'title': title,
                }
                result.append(res)
    return result




def get_request(url, params=None):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36'
    }
    response = requests.get(url, params=params, headers=headers)
    content = response.content.decode('utf-8')
    print(content)
    data = json.loads(content)
    return data.get('data')


def write_csv(data, path):
    with open(path, 'a', encoding='utf-8-sig') as fp:
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
    # result = load_html(FUJIAN_UNIFORM_POLICY_NET)
    # write_csv(result, 'data/policy_fujian.csv')
    load_clear_info('data/policy_fujian.csv')
