# coding: utf-8

import requests
from lxml import html
import mysql.connector
from mysql.connector import errorcode
from datetime import datetime
import mechanize
from bs4 import BeautifulSoup as BS

LOGIN_URL = "http://classroom.dwit.edu.np/login/index.php"
URL = "http://classroom.dwit.edu.np/my/"
SUBJECTURL = "http://classroom.dwit.edu.np/mod/assign/index.php?id="

users = [['saurav.bhandari','Saurav00114#','2019'],['shreyansh.lodha','Sheru!@#45','2018'],['bikash.sapkota','#Dwit123!','2017'],['bandana.aryal','#Bandana123!','2020']]

def getAssignment(url, ins):
    # from cookiejar import CookieJar
    br = mechanize.Browser()
    ret = "None"
    i = 0
    # Browser options
    # Ignore robots.txt. Do not do this without thought and consideration.
    br.set_handle_robots(False)
    # Don't add Referer (sic) header
    br.set_handle_referer(False)
    # Don't handle Refresh redirections
    br.set_handle_refresh(False)
    br.open("http://classroom.dwit.edu.np/login/index.php")
    br.select_form(nr=0)
    br.form['username'] = users[ins][0]
    br.form['password'] = users[ins][1]
    br.submit()
    br.open(url)
    soup = BS(br.response().read(), "lxml")
    for post_text in soup.find_all('div', {'class': 'no-overflow'}):
        ret = post_text.text

    ret.replace("'", "\'")

    return ret

        # print(ret)
        #
        # return ret
        #
        # for post in post_text.find_all('li'):
        #     if post.string != "None":
        #         ret += post.string

    # print(ret)
    # return ret



def main():

    try:
        cnx = mysql.connector.connect(user='root', password='root', host='localhost', database='application')
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)

    mycursor = cnx.cursor()
    mycursor.execute("use application")

    remove_assignment = "delete from assignment where 1"
    # print(add_assignment)
    mycursor.execute(remove_assignment)
    cnx.commit()

    for ins in range(4):

        session_requests = requests.session()

        # Get login csrf token
        result = session_requests.get(LOGIN_URL)
        tree = html.fromstring(result.text)

        # Create payload
        payload = {
            "username": users[ins][0],
            "password": users[ins][1],
        }

        # Perform login
        result = session_requests.post(LOGIN_URL, data=payload, headers=dict(referer=LOGIN_URL))
        print(result)

        # Scrape url
        result = session_requests.get(URL, headers=dict(referer=URL))
        tree = html.fromstring(result.content)

        subjectid = list()

        for subject in tree.xpath("//div[@class='box coursebox']"):
            subjectid.append(subject.attrib['id'][7:])

        for id in subjectid:
            url = SUBJECTURL + id

            try:
                result = session_requests.get(url, headers=dict(referer=URL))
                tree = html.fromstring(result.content)
                subject = tree.xpath('//h1[@class="headermain"]/text()')

                for assignment in tree.xpath('//table[@class="generaltable"]'):
                    assignmentName = assignment.xpath("//td[2]/a/text()")
                    week = assignment.xpath("//td[1]/text()")
                    duedate = assignment.xpath("//td[3]/text()")

                    link = assignment.xpath("//td[2]/a//@href")
                    size = len(assignmentName)

                    # print(link);
                    # mycursor.execute("""insert into assignment(id) values('2')""")
                    # mycursor.commit()


                    get_id=  "SELECT id from assignment"

                    # ass = getAssignment(link[i], ins)

                    # print(ass)

                    for i in range(size):

                        ass = getAssignment(link[i], ins)
                        ass = ass.replace("'", "\\'")
                        print(ass)
                        duedates = (datetime.strptime(duedate[i], "%A, %d %B %Y, %I:%M %p") - datetime(1970,1,1)).total_seconds()
                        # print(duedates)
                        add_assignment = "INSERT INTO assignment(name, assignment,deadline, url, class) VALUES(" + "'" + assignmentName[i] + "' , " + "'" + ass + "' , '" + str(duedates) +  "'"  +"," + "'" + link[i] + "' , '"  +  users[ins][2] + "')"
                        # print(add_assignment)
                        mycursor.execute(add_assignment)
                        cnx.commit()
            except Exception:
                continue

if __name__ == '__main__':
    main()




