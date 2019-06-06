FROM bde2020/spark-submit:2.4.1-hadoop2.7

COPY template.sh /

# Copy the requirements.txt first, for separate dependency resolving and downloading

RUN pip3 install --upgrade pip
RUN pip3 install --upgrade setuptools

# RUN alias python=python3
# ln -s /usr/bin/pip3 /usr/bin/pip
RUN apk add --update python3
# RUN alias python='/usr/bin/python3'
RUN python3 --version
# RUN alias pip=pip3
RUN pip3 --version

ONBUILD COPY requirements.txt /app/
ONBUILD RUN cd /app \
    && pip3 install -r requirements.txt

# Copy the source code
ONBUILD COPY . /app

CMD ["/bin/bash", "/template.sh"]