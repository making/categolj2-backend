# CategolJ2 Backend

[![Build Status](https://travis-ci.org/making/categolj2-backend.svg)](https://travis-ci.org/making/categolj2-backend)

CategoljJ2 is a micro blog system. This backend provides REST API for blog system and admin console to manage these data. You can create any blog frontend using this APIs ;)

The following technologies are used.

* Java 8
* Spring 4
* Spring MVC
* Spring Security OAuth
* Spring Data JPA
* JPA 2.1
* Hibernate Search
* Flyway
* HikariCP
* Backbone.js
* ...

## Quick launch

Download categolj2-backend.jar from [release page](https://github.com/making/categolj2-backend/releases).
This jar is executable becaseuse it includes an embedded Tomcat7.

Options are following:

    $ java -jar categolj2-backend.jar -help
    usage: java -jar [path to your exec war jar]
     -ajpPort <ajpPort>                     ajp port to use
     -clientAuth                            enable client authentication for
                                            https
     -D <arg>                               key=value
     -extractDirectory <extractDirectory>   path to extract war content,
                                            default value: .extract
     -h,--help                              help
     -httpPort <httpPort>                   http port to use
     -httpProtocol <httpProtocol>           http protocol to use: HTTP/1.1 or
                                            org.apache.coyote.http11.Http11Nio
                                            Protocol
     -httpsPort <httpsPort>                 https port to use
     -keyAlias <keyAlias>                   alias from keystore for ssl
     -loggerName <loggerName>               logger to use: slf4j to use slf4j
                                            bridge on top of jul
     -obfuscate <password>                  obfuscate the password and exit
     -resetExtract                          clean previous extract directory
     -serverXmlPath <serverXmlPath>         server.xml to use, optional
     -uriEncoding <uriEncoding>             connector uriEncoding default
                                            ISO-8859-1
     -X,--debug                             debug

These are options of [tomcat-maven-plugin](http://tomcat.apache.org/maven-plugin-2.2/executable-war-jar.html).

**Note that CategoLJ2 requires Java8**.

### Simple usage

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar -httpPort 8080

Access

* Backend 
  * [http://localhost:8080/admin.jsp](http://localhost:8080/admin.jsp)
  * login with initial account (admin/demo)
* Sample frontend 
  * [http://localhost:8080](http://localhost:8080)
  * see [Setup frontend](#setup-frontend) to customize frontend screen.


CategoLJ2 supports H2 and MySQL and embedded H2 is used as default.

You can change H2 data location as following:

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar -httpPort 8080 -Dcategolj2.h2.datadir=./foobar

When you would like to use MySQL then:

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar -httpPort 8080 -Ddatabase=MYSQL -Ddatabase.driverClassName=com.mysql.jdbc.Driver -Ddatabase.url=jdbc:mysql://localhost:3306/categolj2?zeroDateTimeBehavior=convertToNull -Ddatabase.username=root -Ddatabase.password=password

In this case, you have to run `create database categolj2` using `mysql` command in advance.

Other properties are explained in [Configurable properties](#configurable-properties).

## Backend APIs

Supported REST APIs are following.

Resource	| Method	| Path	| Description	| Pageable	| Authenticated
--------------- | ------------- | ----- | ------------- | ------------- | ----------------
Entries	| GET	| /api/v1/entries	| Get all entries	| × | 	
Entries	| GET	| /api/v1/entries?keyword={keyword}	| Search entries	| ×  | 		
Entries	| GET	| /api/v1/categories/{category}/entries	| Get all entries associated with the specified category.		 | ×  |
Entries	| GET	| /api/v1/users/{createdBy}/entries	| Get all entries created with the specified user.	| ×  |
Entries	| POST	| /api/v1/entries	| Create a new entry.	| | ×
Entry	| GET	| /api/v1/entries/{entryId}	| Get the specified entry.	 | |  	
Entry	| PUT	| /api/v1/entries/{entryId}	| Update the specified entry.	 | | ×	
Entry	| DELETE	 | /api/v1/entries/{entryId}	| Delete the specified entry.	| | × 
Entry Histories	| GET	| /api/v1/entries/{entryId}/histories	| Get all entry histories associated with the specified entry. | | × 	
Categories	| GET	| /api/v1/categories	| Get all categories.	| | 	
Categories	| GET	| /api/v1/categories?keyword={keyword}	| Search categories. | | × 		
Recent Posts	| GET	| /api/v1/recentposts	| Get entries updated recently.	 | |
Users	| GET	| /api/v1/users	| Get all users.	 | ×  |  × 	
Users	| POST	| /api/v1/users	| Create a new user.	 | |  × 	
User	| GET	| /api/v1/users/{username}	| Get the specified user. | | ×  		
User	| PUT	| /api/v1/users/{username}	| Update the specified user.  | | ×  		
User	| DELETE	| /api/v1/users/{username}	| Delete the specified user. | | ×  
User	| GET	| /api/v1/users/me	| Get login user.  | | ×
User	| PUT	| /api/v1/users/me	| Update login user. (not implemented yet)  | | ×  		
Links	| GET	| /api/v1/links	| Get all links.	 | | 	
Links	| POST	| /api/v1/links	| Create a new link. | | ×  		
Link	| GET	| /api/v1/links/{url}	| Get the specified link.	 | | 	
Link	| PUT	| /api/v1/links/{url}	| Update the specified link. | | ×  		
Link	| DELETE	| /api/v1/links/{url}	| Delete the specified link. | | ×  		
Files	| GET	| /api/v1/files	| Get all file summaries (not include file contents).  | ×  |  × 
Files	| POST	| /api/v1/files	Upload files. | | ×  
File	| GET	| /api/v1/files/{fileId}	| Get the specified file. | | 
File	| GET	| /api/v1/files/{fileId}?attachment	| Download the specified file (with prompt). | |
File	| DELETE	| /api/v1/files/{fileId}	| Delete the specified file. | |  × 
Books	| GET	| /api/v1/books?title={title}	| Search books by the given title using Amazon Product API		 | | ×  
Books	| GET	| /api/v1/books?keyword={keyword}	| Search books by the given keyword using Amazon Product API | | ×  		

## Admin Console Screenshots

Admin console is a single page application using AJAX/REST API mentioned above. 

### Dashboard

![Dashboard][1]

### Manage entries

#### Entry list
![Entry list][2]

#### Entry form
You can write contents using Markdown or raw HTML.

![Entry form][3]

#### Entry preview
Live preview is available.

![Entry preview][4]

#### File upload
You can upload files to use the entry on the fly.
HTML5 `multiple` attribute is supported.

![File upload][5]

#### Amazon search
You can insert book link using Amazon Product API.

![Amazon search][6]

run with `-Daws.accesskey.id=<Your Accesskey ID for AWS> -Daws.secret.accesskey=<Your Secret Accesskey for AWS> -Daws.associate.tag=<Your Associate Tag>`

#### Entry search
Full text search is available.

![Entry search][7]

### Manage uploaded files

![Manage uploaded files][8]

### Manage users

#### User list

![User list][9]

#### User edit
Inline edit is available.

![User edit][10]

### Manage links

![Manage links][11]

## Setup frontend

The following sample fronted is embedded by default:

![Sample Frontend][12]

You can change the frontend location with `frontend.resources.location` property like following:

    $ echo -n "Hello CategoLJ2" > /tmp/index.html
    $ java -jar categolj2-backend.jar -Dfrontend.resources.location=file:/tmp

The following screen is displayed when you set it in this way.
    
![Custom Frontend][13]

 Note that `frontend.resources.location` must start with `file:` prefix and an absolute path. 

Be careful not to expose  "[**Path Traversal**](https://www.owasp.org/index.php/Path_Traversal)" vulnerability.

When you want to revise HTML/JavaScript of the sample frontend, you extract the file as follows and  please copy these:

    $ unzip categolj2-backend.jar -d tmp
    $ unzip tmp/.war -d categolj2-backend
    $ mv categolj2-backend/categolj2-frontend/* your-frontend-location

For example, 

    $ mkdir -p /tmp/frontend
    $ mv categolj2-backend/categolj2-frontend/* /tmp/frontend/
    $ sed -i -e "s/BLOG.IK.AM/My Blog Title/g" /tmp/frontend/index.html
    $ java -jar categolj2-backend.jar -Dfrontend.resources.location=file:/tmp/frontend

You can get the following screen:
    
![Custom Frontend][14]

## Configurable properties

Key | Description | Default value
--------------- | ------------- | ----- 
log.verbose | set this property `warn` if you want to supress the output of verbose log. | 
log.sql | set this property `debug` if you want to output SQL log. | error
log.sql.result | set this property `debug` if I want to output SQL result log. | error
log.dir | directory to output log | log
log.file.prefix | prefix of log file | categolj2
backend.resources.cache.seconds | cache lifetime of static resources (HTML/JavaScript/CSS/Image) in Backend | 604800
frontend.resources.location | location of static resources in Frontend | categolj2-frontend
frontend.resources.cache.seconds | cache lifetime of static resources (HTML/JavaScript/CSS/Image) in Frontend | 604800
database | database name (H2 or MYSQL) | H2
database.url |  url for JDBC driver| jdbc:h2:file:${categolj2.h2.datadir:/tmp}/categolj2 
database.username | database username | sa
database.password | database password |
database.driverClassName | JDBC Driver class name | org.h2.Driver
hibernate.search.default.indexBase | file path storing Iucene index | /tmp/lucene
cp.maxActive | max active for connection pool | 96
cp.minIdle | min idle for connection pool | 0
cp.maxWait | max wait for connection pool |  60000
aws.accesskey.id | access key ID for AWS | \<Your Accesskey ID for AWS\>
aws.secret.accesskey | secret accesskey for AWS | \<Your Secret Accesskey for AWS\>
aws.endpoint | AWS endpoint| https://ecs.amazonaws.jp
aws.associate.tag | Associate Tag for Amazon Affiliate | ikam-22
accesslog.disabled | Disable to write access logs | false


## License

Licensed under the Apache License, Version 2.0.

  [1]: ./screenshots/ss-dashboadrd.png
  [2]: ./screenshots/ss-entries.png
  [3]: ./screenshots/ss-entries-form.png
  [4]: ./screenshots/ss-entries-preview.png
  [5]: ./screenshots/ss-entries-upload.png
  [6]: ./screenshots/ss-entries-amazon.png
  [7]: ./screenshots/ss-entries-search.png
  [8]: ./screenshots/ss-uploads.png
  [9]: ./screenshots/ss-users.png
  [10]: ./screenshots/ss-users-inline-edit.png
  [11]: ./screenshots/ss-links.png
  [12]: ./screenshots/ss-frontend.png
  [13]: ./screenshots/ss-frontend-custom.png
  [14]: ./screenshots/ss-frontend-custom2.png
