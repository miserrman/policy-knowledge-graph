import jieba.posseg
import requests
t = requests.get("https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=41&dosubmit=1&subcatid=714&start_time=&end_time=&title=")
print(t.text)