import requests
import csv
from bs4 import BeautifulSoup
from datetime import datetime, date, timedelta
from bs4.element import NavigableString
FUJIAN_CITY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList'
FUJIAN_REGION = 'http://api.zwfw.fujian.gov.cn:731/cms-business/otherService/getCityList?sitePunid=#&type=1'
FUJIAN_DEPART = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getDeptByAreaBelongto?type=1&deptUnid=#'
FUJIAN_POLICY = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingService/getQzqdInfoList?stype=&deptOrSiteunid=#&subname=&pageSize=10&pageNum=1'
FUJIAN_POLICY_CONTENT = 'http://api.zwfw.fujian.gov.cn:731/cms-business/listingDetail/getPowerDetail?powerunid=#&type=1'
FUJIAN_UNIFORM_POLICY_NET = 'https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=41&dosubmit=1&subcatid=#&start_time=&end_time=&title='
FQWApplyFor="https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=15&page=%s"
country="https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=40&page=%s"
write_title='../policy/data/try_title.csv'
write_content='../policy/data/try_content.csv'
headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36'
}

def get_pages_url(path):
    '''
    获得所有页码的地址
    :param path:
    :return:
    '''
    urls=[]
    page_request = requests.get(path %(1), params='html', headers=headers)
    page_text = page_request.text
    soup = BeautifulSoup(page_text, 'lxml')
    pagenum = soup.findAll(name='ul', attrs={'class': 'pagination'})[0].contents[23].text
    for i in range(int(pagenum)):
        urls.append(path %(i+1))
    return urls

def get_perdetail_url(urls):
    result=[]
    csv_obj = open(write_title, 'w', newline='', encoding="utf-8-sig")
    csv.writer(csv_obj).writerow(["depart_id","detail","title","date"])
    for url in urls:
        page_request = requests.get(url, params='html',headers=headers)
        page_text = page_request.text
        #print(page_text)
        soup = BeautifulSoup(page_text,'lxml')
        types = soup.findAll(name='div', attrs={'class': 'index_search_select'})[0].contents[1].contents
        typeinfo={}
        for ty in types:
            if not isinstance(ty, NavigableString):
                value = ty.attrs['value']
                typeinfo[value] = ty.text
        infos = soup.findAll(name='div', attrs={'class': 'policies_div'})[0].contents[1].contents
        for info in infos:
            if not isinstance(info, NavigableString):
                deadline=str(info.find(name='span').string)[5:]
                title=info.find('h4').string
                href=info.find('a').attrs['href']
                ty = href[href.find('catid=') + len('catid='):]
                ty = ty[:ty.find('&')]
                res={
                    'type':ty,
                    'href':href,
                    'title':title,
                    'deadline':deadline,
                }
                csv.writer(csv_obj).writerow([ty, href,title,deadline])
                result.append(res)
    csv_obj.close()
    return result

def get_content(result):
    '''
    获得政策内容
    :param result:
    :return:
    '''
    # csv_obj = open(write_content, 'w', newline='', encoding="utf-8-sig")
    # csv.writer(csv_obj).writerow(["title","content"])
    contents=[]
    for res in result:
        try:
            page_request = requests.get(res['href'], params='html',headers=headers)
            page_text = page_request.text
            #print(page_text)
            soup = BeautifulSoup(page_text,'lxml')
            s = soup.find(name='div', attrs={'class': 'details_title'})
            title=s.contents[1].text
            content_list = soup.find(name='div', attrs={'id': 'source'})
            content = ''
            htmlcon = ''
            if content_list:
                for para in content_list.contents:
                    if not isinstance(para, NavigableString):
                        content += para.text if para.text else ''
                        htmlcon += str(para) if para else ''
            else:
                content ='空'
        except requests.exceptions.ConnectionError as e:
            title="空"
            content="空"
        # csv.writer(csv_obj).writerow([title, content])
        con={
            'title':title,
            'content':content,
            'htmlcon':htmlcon
        }
        contents.append(con)
    # csv_obj.close()
    return contents

def addnew_perdetail_url(urls):
    '''
    获得规定时间内具体政策的网址
    :param urls:
    :return:
    '''
    result=[]
    exitflag = False
    for url in urls:#每一页
        page_request = requests.get(url, params='html',headers=headers)
        page_text = page_request.text
        #print(page_text)
        soup = BeautifulSoup(page_text,'lxml')
        types = soup.findAll(name='div', attrs={'class': 'index_search_select'})[0].contents[1].contents
        typeinfo={}
        for ty in types:
            if not isinstance(ty, NavigableString):
                value = ty.attrs['value']
                typeinfo[value] = ty.text
        infos = soup.findAll(name='div', attrs={'class': 'policies_div'})[0].contents[1].contents
        for info in infos:#每一页所有的文章
            if not isinstance(info, NavigableString):
                articleDate=str(info.find(name='span').string)[5:]

                title=info.find('h4').string
                href=info.find('a').attrs['href']
                ty = href[href.find('catid=') + len('catid='):]
                ty = ty[:ty.find('&')]
                #判断时间限期
                if  Intimerange(articleDate) == True:
                    res={
                        'deadline':articleDate,
                        'title':title,
                        'href':href,
                        'type':ty,
                    }
                    # csv.writer(csv_obj).writerow([ty, href,title,articleDate])
                    result.append(res)
                else:
                    exitflag = True
                    break
        if exitflag == True:
            break
    return result

def Intimerange(articleDate):
    '''
    判断时间限制
    :param articleDate:
    :return:
    '''
    yearindex = articleDate.find('-')
    article_year = int(articleDate[:yearindex])
    monthindex = articleDate.find('-', yearindex+1)
    article_month = int(articleDate[yearindex+1:monthindex])
    article_day = int(articleDate[monthindex+1:])

    articleDate = datetime(article_year,article_month,article_day,0,0)#文章日期
    nowDate = datetime.now()
    nowDate = datetime(nowDate.year, nowDate.month, nowDate.day,0,0)#当前日期
    #范围更改
    if (nowDate - articleDate).days <= 40:#相差天数
        print(articleDate)
        return True
    return False

if __name__ == '__main__':
    urls = get_pages_url(country)
    detailurls = addnew_perdetail_url(urls)
    contents = get_content(detailurls)