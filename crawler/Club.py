from time import sleep
from selenium import webdriver
from io import BytesIO
from bs4 import BeautifulSoup
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import json
import os
import re
from datetime import datetime, timedelta, date

options = Options()
options.add_argument('--headless')               # headless
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--disable-gpu')
options.add_argument('--window-size=1920x1080')
options.add_experimental_option('excludeSwitches', ['enable-logging'])

driver = webdriver.Chrome(options=options)
driver.implicitly_wait(3)
driver.get('https://linkareer.com/robots.txt')


def parse_simple_content(text):
    global parsed_data
    lines = text.split('\n')
                    
    # Parse key-value pairs
    for i in range(0, len(lines), 2):
        key = lines[i]
        value = lines[i+1]
        parsed_data[key] = value
        
def parse_recruitment_info(text):
    recruitment_info = {}

    sections = text.split('  ')

    for section in sections:
        section = section.strip()
        if not section:
            continue
        lines = section.split('\n')
        title = lines[0].strip()
        content = ' '.join(lines[1:]).strip()
        recruitment_info[title] = content

    return recruitment_info



parsing_data = {}
result={}
BASE_DIR = os.path.dirname(os.path.abspath(__file__))



current_date = date.today()
one_month_ago = current_date - timedelta(days=30)

sitemap=driver.find_element(By.CSS_SELECTOR,'body > pre')

urls = re.findall(r"https://[^\s]+", sitemap.text)

totalsite=len(urls)-4 #  쓸때없는 coverletter 4개 값 빼야함

for i in range(totalsite-1,totalsite-6,-1): ## 80부터가 최신이므로 80번 부터 탐색
    data=1
    driver2 = webdriver.Chrome(options=options)
    driver2.implicitly_wait(1)
    driver2.get(urls[i])  
    
    ## 1 4 7 순으로 폴더가 늘어남
    while True:
        result={}
        try:
            if data>3000: # 파일 최대갯수 3천개이므 3천개 넘어가면 break
                break
            
            linkareer_site=driver2.find_element(By.CSS_SELECTOR,'#folder{} > div.opened > div:nth-child(2) > span:nth-child(2)'.format(data))
            linkarrer_date=driver2.find_element(By.CSS_SELECTOR,'#folder{} > div.opened > div:nth-child(8) > span:nth-child(2)'.format(data))
             
            # 공고올린 날짜를 날짜데이터로 변환 
            target_date = datetime.strptime(linkarrer_date.text, '%Y-%m-%dT%H:%M:%S.%fZ').date()
            print("공고올린 날짜 : ", target_date,"현재날짜 : ",current_date)
            if linkareer_site.text!="" and target_date >= one_month_ago and target_date <= current_date: ## 1달내로 올린 글이고, folder가 비지않았으면 계속 탐색
                driver3 = webdriver.Chrome(options=options)
                driver3.implicitly_wait(2)
                driver3.get(linkareer_site.text) # 링커리어 대외활동페이지 오픈
            
                ## 공모전인지 대외활동인지 동아리인지 채용 인턴인지 확인
            
                header=driver3.find_element(By.CSS_SELECTOR,'#__next > div.jss2.PageLayout__StyledWrapper-sc-8e20e380-0.hyrgHR > div.internal-nav-bar.InternalNavBar__StyledWrapper-sc-3b9fd182-0.jxFbnW > div > div.menu-list > button:nth-child(3) > div.second-depth-nav > div > div > div > button:nth-child(5)')

            
                if header.get_attribute("data-active")=="true": 
                    print("동아리에 대한 정보에 들어왔습니다")
                    print(linkareer_site.text)
                    
                    img=driver3.find_element(By.CSS_SELECTOR,'#__next > div.jss2.PageLayout__StyledWrapper-sc-8e20e380-0.hyrgHR > div.MuiContainer-root.jss10.jss1.MuiContainer-maxWidthLg > div > div.jss3 > div.jss4.jss5 > div:nth-child(1) > div.jss49 > div.jss50.jss51 > div > div > figure > img')
                    title=driver3.find_element(By.CSS_SELECTOR,'#__next > div.jss2.PageLayout__StyledWrapper-sc-8e20e380-0.hyrgHR > div.MuiContainer-root.jss10.jss1.MuiContainer-maxWidthLg > div > div.jss3 > div.jss4.jss5 > div:nth-child(1) > h1')
                    content=driver3.find_element(By.CSS_SELECTOR,'#__next > div.jss2.PageLayout__StyledWrapper-sc-8e20e380-0.hyrgHR > div.MuiContainer-root.jss10.jss1.MuiContainer-maxWidthLg > div > div.jss3 > div.jss4.jss5 > div:nth-child(1) > div.jss49 > div:nth-child(2) > div.jss68')
                    detail_content=driver3.find_element(By.CSS_SELECTOR,'#__next > div.jss2.PageLayout__StyledWrapper-sc-8e20e380-0.hyrgHR > div.MuiContainer-root.jss10.jss1.MuiContainer-maxWidthLg > div > div.jss3 > div.jss4.jss5 > div:nth-child(4) > div:nth-child(1) > div.jss147 > div:nth-child(2)')
                    result["이미지"]=img.get_attribute('src')
                    result['제목 ']=title.text
                    
                    parsed_data={}

                    lines = content.text.split('\n')

                    
                    summary_information={}
                    parse_simple_content(content.text)
                    # 모집내용 파싱
                    # 파싱된 데이터 result에 할당
                    for key, value in parsed_data.items():

                        # print(key + ': ' + value)
                        summary_information[key]=value
                    

                        
                    # 상세내용은 inner html코드로 가져오기
                    parsed_info = detail_content.get_attribute('innerHTML')
                    
                    #요약정보 데이터 result 에할당
                    result["요약정보"]=summary_information
                    # 상세내용 데이터 result에 할당
                    result["상세내용"]=parsed_info
                    result['Category']="동아리"
                        # print(title,content)

                    # primary key로 잡을 데이터를 linkareer_site domain 번호로 잡았음.
                    parsing_data[linkareer_site.text]=result
   


                
                else:
                    print("동아리에 대한 정보가 아닙니다 재탐색 합니다.")
                
            
            else: # ## 1달내로 올린 글이 아니고, folder가 비어 있음
                break
        
            
        except:
            pass
        data+=816 # 원래는 폴더가 3씩 늘어나서 +=3으로해야함.
            

# json 파일로 변환하는 작업
with open(os.path.join(BASE_DIR, 'Club.json'), 'w+',encoding='utf-8') as json_file:
    json.dump(parsing_data, json_file, ensure_ascii = False, indent='\t')

print("완료!")
