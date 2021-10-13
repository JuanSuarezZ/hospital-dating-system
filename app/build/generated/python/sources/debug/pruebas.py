
import psycopg2


        
con = psycopg2.connect(
database="enlcuxzb",
user="enlcuxzb",
password="bUgzbxegz3BGkRNfRPqvY8y-XpAjy8Mj",
host="ziggy.db.elephantsql.com",
port="5432"
            )

cursor = con.cursor()
query = '''select * from proveedor'''
cursor.execute(query)
con.commit()
rows = cursor.fetchall()
con.close()
b = {'mensaje': rows}
print(b)  


         