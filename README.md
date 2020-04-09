# minio-private-storage

## exec_minio_docker
- É encontrado um arquivo (docker-compose.yaml) para ser executado via docker-compose que cria '8 máquinas' com o serviço MinIO, sendo duas zonas cada uma com 4 máquinas, acessíveis pelo navegador nos endereços 127.0.0.1:(9001 até 9008). O próprio MinIO oferece uma interface web onde podem ser executadas diversas ações como fazer upload/download de objetos e criar/excluir buckets.
- Estão configurados os valores **'minio'** e **'minio123'** como **access key** e **secret key** respectivamente.
- Ao executar o docker-compose é criado também um Nginx que faz proxy e load balance para o cluster MinIO. O arquivo 'nginx.conf' tem detalhes da configuração do Nginx. Através do browser no endereço localhost:9000 é possível ter acesso ao cluster passando pelo Nginx.
- Para excutar, navegue pelo terminal até a pasta onde se encontram os arquivos e execute os comandos:
 ```
 sudo docker-compose pull
 sudo docker-compose up
 ```
 - Para encerrar, Ctrl + c
 
 ## minioPrivateStorage
 - Se trata de um projeto em java desenvolvido com Spring Boot que cria um serviço Rest que se conecta ao cluster MinIO através do proxy nginx.
