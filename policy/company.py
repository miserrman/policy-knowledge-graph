# -*- coding:utf-8 -*-
import pandas as pd

people = pd.read_csv("data/company/企业从业人员.csv", header=0, usecols=["年份", u"企业名称", u"科研人员数"])
document = pd.read_csv("data/company/企业管理档案_20210416.csv", header=0, usecols=["企业名称", "企业规模"])
certificates = pd.read_csv("data/company/企业资质证书.csv", header=0, usecols=["企业名称", "证书名称"])
profit = pd.read_csv("data/company/企业财务.csv", header=0, usecols=["年份", "企业名称", "行业类型", "年度利润(万元)", "年度研发经费投入(万元)", "年度高新技术产品收入(万元)"])
total = pd.merge(profit, people)
total.drop_duplicates(subset=['企业名称'], keep='first', inplace=True)
total = pd.merge(total, document)
total['证书名称'] = None
scale_map = {
    "微型": 0,
    "小型": 1,
    "中型": 2,
}

for index, row in total.iterrows():
    name = row["企业名称"]
    chart = certificates[certificates['企业名称'] == name]
    cer = []
    chart.drop_duplicates(subset=["证书名称"], keep='first', inplace=True)
    for i, r in chart.iterrows():
        cer.append(r["证书名称"])
    cer_str = ','.join(cer)
    total.iloc[index, -1] = cer_str

res = []

industry_map = {}
industry = total[["企业名称", "行业类型"]]
industry.drop_duplicates(subset=['行业类型'], inplace=True)
i = 1
for index, row in industry.iterrows():
    industry_map[row['行业类型']] = i
    i += 1

with open('data/company/industry.sql', 'w', encoding='utf-8') as fp:
    for key in industry_map.keys():
        s = "insert into industry(name) values(\"{name}\");"
        p = {
            'name': key
        }
        insert = s.format(s, **p)
        fp.write(insert)
        fp.write('\n')

print(industry_map)
for index, row in total.iterrows():
    name = row["企业名称"]
    region = name[0:2]
    industry_id = industry_map.get(row["行业类型"])
    profit = row["年度利润(万元)"]
    researchExpense = row["年度研发经费投入(万元)"]
    techOutfit = row["年度高新技术产品收入(万元)"]
    scale = row["企业规模"]
    c = row["证书名称"]
    researchersNum = row["科研人员数"]
    insert_str = "insert into enterprise" \
                 "(name,region,profit,research_expense,tech_outfit," \
                 "industry_id,researchers_num,scale,certificates)" \
                 "values (\"{name}\",\"{region}\",{profit},{research_expense}," \
                 "{tech_outfit},{industry_id},{researchers_num},{scale},{certificates});"
    param = {
        'name': name,
        'region': region,
        'profit': profit,
        'research_expense': researchExpense,
        'tech_outfit': techOutfit,
        'industry_id': industry_id,
        'researchers_num': researchersNum,
        'scale': scale_map.get(scale),
        'certificates': "\"" + c + "\"" if c else "NULL"
    }
    r = insert_str.format(insert_str, **param)
    res.append(r)

with open("data/company/enterprise.sql", 'w', encoding='utf-8') as fp:
    for r in res:
        fp.write(r)
        fp.write('\n')


