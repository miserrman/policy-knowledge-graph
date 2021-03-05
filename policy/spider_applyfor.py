import requests
import json
import csv
import pandas as pd
import codecs
from bs4 import BeautifulSoup
import re
from lxml import etree
from bs4.element import NavigableString
FUJIAN_CITY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList'
FUJIAN_REGION = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList?sitePunid=#&type=1'
FUJIAN_DEPART = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getDeptByAreaBelongto?type=1&deptUnid=#'
FUJIAN_POLICY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getQzqdInfoList?stype=&deptOrSiteunid=#&subname=&pageSize=10&pageNum=1'
FUJIAN_POLICY_CONTENT = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingDetail/getPowerDetail?powerunid=#&type=1'
FUJIAN_UNIFORM_POLICY_NET = 'https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=41&dosubmit=1&subcatid=#&start_time=&end_time=&title='
FQWApplyFor="https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=15&page=%s"
write_applyfor='data/policy_applyfor.csv'

headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36'
}



def get_pages_url(path):
     urls=[]
     page_request = requests.get(path %(1), params='html', headers=headers)
     page_text = page_request.text
     soup = BeautifulSoup(page_text, 'lxml')
     pagenum = soup.findAll(name='ul', attrs={'class': 'pagination'})[0].contents[23].text
     for i in range(int(pagenum)):
         urls.append(path %(i+1))
     # urls.append(path % 64)
     return urls

def get_perdetail_url(urls):
    hrefs=[]
    titles=[]
    result=[]
    for url in urls:
        page_request = requests.get(url, params='html',headers=headers)
        page_text = page_request.text
        #print(page_text)
        soup = BeautifulSoup(page_text,'lxml')
        infos = soup.findAll(name='div', attrs={'class': 'policies_div'})[0].contents[1].contents
        for info in infos:
            if not isinstance(info, NavigableString):
                deadline=info.find(name='td').contents[3].text
                title=info.find('h4').string
                href=info.find('a').attrs['href']
                res={
                    'deadline':deadline,
                    'title':title,
                    'href':href,
                }
                result.append(res)
    return result


def get_content(result):
    contents=[]
    titles=[]
    i=0
    csv_obj = open(write_applyfor, 'w', newline='', encoding="utf-8-sig")
    csv.writer(csv_obj).writerow(["deadline","title","content"])
    for res in result:
        try:
            page_request = requests.get(res['href'], params='html',headers=headers)
            page_text = page_request.text
            #print(page_text)
            soup = BeautifulSoup(page_text,'lxml')
            s = soup.findAll(name='div', attrs={'id': 'source'})
            if len(s) == 0:
                s=soup.findAll(name='div', attrs={'class': 'text'})
                if len(s) == 0:
                    content ="空"
                    continue
            content = s[0].text
            # print(content)
        except requests.exceptions.ConnectionError as e:
            content="空"
            print("空")
        csv.writer(csv_obj).writerow([res['deadline'],res['title'], content])
    csv_obj.close()


if __name__ == '__main__':
    urls = get_pages_url(FQWApplyFor)
    result = get_perdetail_url(urls)
    get_content(result)