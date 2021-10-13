import numpy as np
import psycopg2
import json
import random
from io import BytesIO
from PIL import Image
import PIL, requests

con = psycopg2.connect(
            dbname="pcomponentes4",
            user="postgres",
            password="123456",
            host="localhost",
            port="5432"
        )


with open("https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQEcurtFvbiWlg3p1WnyJ0NcvWiV8mszcTNPFY3L9A7u_SpxqCAAIN1pIoHtKx83rAHuMmQDXqwrvBn-ko4p_QhY-Sr65ebuCWKtJMCa2Ln&usqp=CAE", "rb") as image:
  f = image.read()
  b = bytearray(f)
  #print (b)


'''
with BytesIO() as output:
    with Image.open("C:/foto.png") as img:
        img.save(output, 'BMP')
    data = output.getvalue()

print(type(data))
'''

#url = 'https://thumbs.dreamstime.com/z/interior-elegante-de-la-sala-estar-con-las-decoraciones-del-terciopelo-un-sof%C3%A1-azul-grande-c%C3%B3modo-y-oro-foto-verdadera-127604109.jpg'
#respuesta = requests.get(url)

#print(type(respuesta.content))
imagen = Image.open(BytesIO(b))
imagen = imagen.resize([150,150])

imagen_arreglo = np.asarray(b)


cursor = con.cursor()
cursor.execute("insert into muebleprueba(id_proveedor,material,cod_de_mueble,imagen,color,precio,dimensiones) values (1,'madera', %s, '123','azul',1233,'1x2')",(psycopg2.extensions.BYTES(imagen_arreglo)))

con.commit()
con.close()
        

print(imagen_arreglo)

imagen_o = PIL.Image.fromarray(np.uint8(imagen_arreglo))
print(type(imagen_o))
Image.Image.show(imagen_o)
