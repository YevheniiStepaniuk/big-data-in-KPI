FROM bde2020/spark-submit:2.4.1-hadoop2.7

COPY template.sh /

RUN apk add --no-cache python3-dev libstdc++ && \
    apk add --no-cache g++ && \
    ln -s /usr/include/locale.h /usr/include/xlocale.h && \
    pip3 install numpy && \
    pip3 install pandas

# Copy the requirements.txt first, for separate dependency resolving and downloading

# RUN alias python=python3
# ln -s /usr/bin/pip3 /usr/bin/pip
# RUN apk add --update python3
# RUN alias python='/usr/bin/python3'
# RUN python3 --version
# RUN alias pip=pip3
# RUN pip3 --version

ONBUILD COPY requirements.txt /app/
ONBUILD RUN cd /app \
    && pip3 install -r requirements.txt

# Copy the source code
ONBUILD COPY . /app

CMD ["/bin/bash", "/template.sh"]