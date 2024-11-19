# ms-c

### Пример мутационного тестирования
```txt
1) pom.xml:
  - spring-boot-starter-test
  - плагин org.pitest
      - зависимость pitest для junit5: pitest-junit5-plugin
      - targetClasses - пакет классов для мутационного тестирования
      - targetTests - пакет классов с тестами для мутационного тестирования
2) ToLowerCaseService - тестируемый класс
3) ToLowerCaseServiceTest - класс с unit-тестами
4) Выполнение тестирования:
   Maven: 
      ms-c:
        Lifecycle
          - clean
          - install
        Plugins:
          - pitest
              - pitest:mutationCoverage    
5) Результаты:
   target\pit-report\com.prosoft.msc.service 
                       - index.html - Pit Test Coverage Report  
                       - ToLowerCaseService.java.html - отчет по классу           
```