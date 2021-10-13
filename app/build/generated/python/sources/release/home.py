from flask import Flask,request

#from flask_socketio import SocketIO, send
from ingresar import BD

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'



'''
    listaCliente = []

    socketio = SocketIO(app)

    @socketio.on('connect')
    def handleMessage():
        
        currentSocketId = request.sid

        a = {'message': 'mensaje de bienvenida'} 
        socketio.emit('message',a,currentSocketId)
    
        print("identificador = ",currentSocketId)    
        print("Conectado!")

    

    @socketio.on('newCX')
    def newClient(socketCX):
        
        print("el socket es = ",socketio)


    @socketio.on('message')
    def Message(mensaje):
        
        a = {'message': 'respuesta socket a cliente'} 
        socketio.emit('message',a)
        print("message= ",mensaje)
'''


@app.route('/')
def hello_world():

    return 'Hello from Flask xd!'


@app.route('/registrarP', methods=["POST"])
def ingresarP():

    data = request.json
    bd = BD()
    return bd.nuevoPaciente(data)

@app.route('/registrarDoc', methods=["POST"])
def ingresarD():

    data = request.json
    bd = BD()
    return bd.nuevoMedico(data)

@app.route('/login', methods=["POST"])
def loginP():


    data = request.json

    print(data)
    bd = BD()
    return bd.logPaciente(data)



@app.route('/loginD', methods=["GET"])
def loginD():

    nombreUser = request.args.get('nombreu')
    contra = request.args.get('contra')
    bd = BD()
    return bd.logDoctor(nombreUser,contra)

@app.route('/recuperacionM', methods=["GET"])
def recuperacion():

    correo = request.args.get('correo')  
    bd = BD()
    return bd.recuperacionM(correo)

@app.route('/CambiarM', methods=["POST"])
def CambiarM():

    data = request.json
    bd = BD()
    return bd.CambiarM(data)

@app.route('/recuperacionP', methods=["GET"])
def recuperacionP():

    correo = request.args.get('correo')  
    bd = BD()
    return bd.recuperacionP(correo)

@app.route('/CambiarP', methods=["POST"])
def CambiarP():

    data = request.json
    bd = BD()
    return bd.CambiarP(data)

@app.route('/verDatosDoc', methods=["GET"])
def verDatosDoc():

    userN = request.args.get('userN')  
    bd = BD()

    return bd.verDatosDoc(userN)

@app.route('/CitasPecDoc', methods=["GET"])
def CitasPecDoc():

    fecha = request.args.get('fecha')
    idDoc = request.args.get('id_Doc')  
    bd = BD()
    return bd.verCitasPenDoc(idDoc,fecha)

@app.route('/UpdateCita', methods=["POST"])
def UpdateCita():

    data = request.json
    bd = BD()
    return bd.UpdateCita(data)

@app.route('/verEspecialidades', methods=["GET"])
def verEspecialidades():

    bd = BD()
    return bd.verEspecialidades()


@app.route('/RegistroEsp', methods=["POST"])
def RegistroEsp():
    
    data = request.json
    bd = BD()
    return bd.RegistroEsp(data)


@app.route('/BuscarCitas', methods=["POST"])
def BuscarCitas():
    
    data = request.json
    bd = BD()
    return bd.BuscarCitas(data)

@app.route('/verPacientes', methods=["POST"])
def verPacientes():
    
    data = request.json
    bd = BD()
    return bd.verPacientes(data)


@app.route('/historiasMedicas', methods=["POST"])
def historiasMedicas():

    data = request.json
    bd = BD()
    return bd.historiasMedicas(data)


@app.route('/verDataPac', methods=["POST"])
def verDataPac():

    data = request.json
    bd = BD()
    return bd.verDataPac(data)

@app.route('/mishistoriasMedicas', methods=["POST"])
def mishistoriasMedicas():

    data = request.json
    bd = BD()
    return bd.mishistoriasMedicas(data)

@app.route('/verDocEspecialidad', methods=["POST"])
def verDocEspecialidad():

    data = request.json
    bd = BD()
    return bd.verDocEspecialidad(data)

@app.route('/AgregarCita', methods=["POST"])
def AgregarCita():

    data = request.json
    bd = BD()
    return bd.AgregarCita(data)

@app.route('/verDoctores', methods=["GET"])
def verDoctores():

    bd = BD()
    return bd.verDoctores()


@app.route('/verImg', methods=["GET"])
def verImg():

    bd = BD()
    return bd.verImg()


if __name__ == "__main__":
    app_flask = app
    app_flask.run(debug=True,host="0.0.0.0")



