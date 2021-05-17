FROM debian:buster-slim

RUN apt-get update \
    && apt-get install -y git curl xz-utils \
    && mkdir -p /var/run/sshd \
    && groupadd -r jenkins \
    && useradd -r -m -g jenkins jenkins \
    && echo "jenkins:jenkins" | chpasswd \
    && curl -L https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jdk_x64_linux_8u292b10.tar.gz \
            | tar -C /home/jenkins -x -z -f - \
    && curl -L https://downloads.apache.org/ant/binaries/apache-ant-1.10.10-bin.tar.xz \
            | tar -C /home/jenkins -J -x -f -

CMD ["/bin/sh"]