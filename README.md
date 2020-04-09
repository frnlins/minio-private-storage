# minio-private-storage

## exec_minio_docker
- É encontrado um arquivo (docker-compose.yaml) para ser executado via docker-compose que cria '8 máquinas' com o serviço MinIO, sendo duas zonas cada uma com 4 máquinas, acessíveis pelo brownser nos endereços 127.0.0.1:(9001 até 9008).
- Ao executar o docker-compose é criado também um Nginx que faz proxy e load balance para esse cluster MinIO. O arquivo 'nginx.conf' faz a configuração do nginx. Através do browser no endereço localhost:9000 é possível ter acesso ao cluster pelo Nginx.
- Estão configurados os valores 'minio' e 'minio123' como access key e secret key respectivamente.
