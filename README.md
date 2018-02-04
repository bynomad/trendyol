# trendyol
## Projeyi çalıştırmak için
Projede Cassandra ve Redis docker üzerinden çalışmakta.
 
Bunları docker-compose up komutu ile çalışır hale getiriyoruz. Ardından projeyi çalıştırabiliriz.

## Client uygulaması
Uygulama https://github.com/bynomad/trendyol-client üzerinde

Application1 uygulamasında çalışacak olan kütüphanedir. kütüphane jarını maven local repository de tutuyoruz ve Application1
 uygulamasında dependency olarak ekleniyor.
 
 mvn install komutu ile ilgili jarı local repositorye ekliyoruz.
 
 ## Application1 uygulaması
 
 Uygulama https://github.com/bynomad/trendyol-Application1 üzerinde
 
 Uygulamayı çalıştırdığımızda trendyol-client kütüphanesi üzerinden ana uygulama üzerindeki bilgileri çekebiliyoruz.
 
 application.properties deki client.time.interval ile ne sıklıkla ana uygulamadan veri çekeceğini belirleyebiliyoruz
. application.name ile hangi uygulama için veri çekileceğini belirtiyoruz.