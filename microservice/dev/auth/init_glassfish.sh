#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

###################################################################
#title          :
#description    :
#author         : Yannick Englert
#email          : yannick.englert@students.ffhs.ch
#date           :
#version        :
#usage          :
###################################################################

print_script_header() {
    local script_name
    local script_description
    local script_author
    local script_email
    local script_date
    local script_version
    local script_usage

    # Extract header information
    while IFS= read -r line || [ -n "$line" ]; do
        case "$line" in
            "#title"*) script_name="${line#*: }" ;;
            "#description"*) script_description="${line#*: }" ;;
            "#author"*) script_author="${line#*: }" ;;
            "#email"*) script_email="${line#*: }" ;;
            "#date"*) script_date="${line#*: }" ;;
            "#version"*) script_version="${line#*: }" ;;
            "#usage"*) script_usage="${line#*: }" ;;
        esac
    done < "$0"

    # Print header information
    echo "Script Name    : $script_name"
    echo "Description    : $script_description"
    echo "Author         : $script_author"
    echo "Email          : $script_email"
    echo "Date           : $script_date"
    echo "Version        : $script_version"
    echo "Usage          : $script_usage"
}

err() {
    echo "[$(date +'%Y-%m-%dT%H:%M:%S%z')]: $*" >&2
}

asadmin start-domain
asadmin add-library /opt/mysql-connector.jar
asadmin create-jdbc-connection-pool --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource --restype javax.sql.DataSource --property user=root:password=YourPassword:DatabaseName=auth:ServerName=auth-mysql:port=33060:UseSSL=false MySQLPool
asadmin create-jdbc-resource --connectionpoolid=MySQLPool jdbc/bloggerResource
asadmin stop-domain
