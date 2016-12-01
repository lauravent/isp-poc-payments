FROM registry.ng.bluemix.net/ibmliberty:webProfile7

MAINTAINER Roberto Pozzi <roberto_pozzi@it.ibm.com>

# Update libs
RUN apt-get update \ 
	&& apt-get -y upgrade \ 
	&& apt-get -y autoclean \ 
	&& apt-get -y autoremove \ 
	&& rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ADD target/isp-poc-payments.war /config/dropins/

EXPOSE :9080

CMD ["/opt/ibm/docker/docker-server", "run", "defaultServer"]