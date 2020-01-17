Protocolo:


Inicio

Inicio de servidor esperando conexiones

Cliente inicia y conecta con el servidor, esperando del servidor la señal de preparado (0 hold, 1 ok)
 
Servidor acepta conexion y envia señal de preparado
 
Cliente recibe señal servidor preparado y solicita ordenes del usuario

usuario introduce operacion a calcular, 

cliente envia al servidor los 2 numeros a calcular y el operando  (1 = "+", 2 = "-", 3 = "*", 4 = "/", 5 = "%", ) 2 floats y 1 int

cliente espera respuesta del servidor

servidor envia primero confirmacion de la operacion (0 error, 1 ok) 
si ok, tambien envia resultado

servidor espera orden de finalizar o reiniciar para un calculo nuevo  (0 o 1)

cliente pregunta al usuario si continuar o finalizar, y envia dato introducido (int) al servidor

servidor lee y se apaga o se reinicia.
