cat /docker-entrypoint-initdb.d/init.sql | \
    sed -r 's/\$\{USERNAME\}/'"${USERNAME}"'/' | \
    sed -r 's/\$\{PASSWORD\}/'"${PASSWORD}"'/' | \
    psql -v ON_ERROR_STOP=1
