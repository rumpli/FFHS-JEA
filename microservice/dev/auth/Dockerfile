FROM omnifish/glassfish
ARG WAR_FILE=*.war
COPY ${WAR_FILE} ./glassfish/domains/domain1/autodeploy/
COPY mysql-connector.jar /opt/
COPY init_glassfish.sh /opt/
RUN /opt/init_glassfish.sh
#RUN sed -i 's|<property name="User" value="APP"></property>|<property name="User" value="root"></property>|g' ./glassfish/domains/domain1/config/domain.xml
#RUN sed -i 's|<property name="Password" value="APP"></property>|<property name="Password" value="YourPassword"></property>|g' ./glassfish/domains/domain1/config/domain.xml
#RUN sed -i 's|<property name="serverName" value="localhost"></property>|<property name="serverName" value="auth-mysql"></property>|g' ./glassfish/domains/domain1/config/domain.xml
#RUN sed -i 's|<property name="PortNumber" value="1527"></property>|<property name="PortNumber" value="33060"></property>|g' ./glassfish/domains/domain1/config/domain.xml
#RUN sed -i 's|<property name="DatabaseName" value="sun-appserv-samples"></property>|<property name="DatabaseName" value="auth"></property>|g' ./glassfish/domains/domain1/config/domain.xml
#RUN sed -i '/<property name="DatabaseName" value="auth"><\/property>/a \      <property name="ssl" value="false"><\/property>' ./glassfish/domains/domain1/config/domain.xml
EXPOSE 8080 4848
CMD ["./bin/asadmin", "start-domain", "-v"]
