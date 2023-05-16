# Geolocalización de IPs
Para ejecutar el programa copie el archivo IpInformation.jar junto con el archivo de configuracion config.properties en alguna carpeta de su PC. Para ejecutar el mismo ejecutar la siguiente línea de comandos estando en el path donde se encuentren los archivos antedichos:

java -jar .\IpInformation.jar {ip}

donde {ip} es la ip que se quiere consultar (igualmente si no se especifica el sistema la pide mediante un menú).


# Configuración
Mediante el archivo config.properties se pueden configurar las Apis a consultar (por el momento sólo están disponibles las que se listan más adelante), las credenciales de las mismas como así también el repositorio (MongoDB o memoria) y el broker de mensajes (RabbitMQ o ninguno).

Para el caso de las Apis si no se especifica una en particular el sistema informará un valor por default (No Data o cero en el caso de números), si se ingresa una que no cuente con la información que se le pide también informará valores por defecto como en el caso anterior. Tener en cuenta que algunos de estos servicios externos se pueden usar de forma gratuita y tienen un límite de consultas, para esto el sistema cuenta con una cantidad de fija de reintentos y si no obtiene la información requerida muestra una por defecto que da razón de que no se pudo obtener lo pedido.

Por otro lado, para la configuración de la base de datos se requiere que se especifique si se utilizará MongoDB  junto con su usuario (DB_USERNAME), contraseña (DB_PASSWORD), CLUSTER (por ejemplo podría ser: cluster0.xok8qml.mongodb.net/basesMeli) nombre de la colección (DB_PASSWORD) (en este caso debe ingresarse el valor REPOSITORY=MongoDB, de otra forma se utilizará la persistencia en memoria y sólo se mantendrá esa información en tiempo de ejecución).

Por último se puede especificar el broker de mensajes deseado (por el momento sólo RabbitMQ), para esto deberá ingresarse el siguiente valor: MESSAGE_BROKER=RabbitMQ, junto con el nombre de la cola en MB_QUEUE_NAME, el host en MB_HOST y el puerto MB_PORT. Si estos últimos 3 no se especifican se utilizarán stadistics, localhost y 5672 para cada uno de ellos. La mejora que trae contar con este broker es la de no depender del tiempo de persistencia en caso de que haya muchas consultas al mismo tiempo.

# Apis utilizadas
Las Apis que se utilzan para obtener la información son las siguientes:
 * https://ipgeolocation.abstractapi.com/v1/
 * http://api.ipapi.com/api
 * https://api.apilayer.com/fixer/latest

Las primeras dos tienen información de la ip mientras que la última se utiliza para obtener la cotización del dólar.
