FROM debian:buster-slim

RUN apt-get update \
    && apt-get install -y git curl xz-utils \
    && groupadd -r jenkins \
    && useradd -r -m -g jenkins jenkins \
    && curl -L https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jdk_x64_linux_8u292b10.tar.gz \
            | tar -C /home/jenkins -x -z -f - \
    && curl -L https://downloads.apache.org/ant/binaries/apache-ant-1.10.10-bin.tar.xz \
            | tar -C /home/jenkins -J -x -f -

ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/home/jenkins/apache-ant-1.10.10/bin:/home/jenkins/openjdk-8u292-b10/bin

CMD ["/bin/sh"]
