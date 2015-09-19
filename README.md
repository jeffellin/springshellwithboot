Spring Boot and Spring Shell don't exactly play well together.  This is because Spring Shell has its own ideas about
how the context is created. In order to make the two tools work together some changes are required in order to pass an 
existing context to the Shell.

This is a sample project of how to make it work.  Some changes are required to the BootStrap class.

Ideally the BootStrap class would be eliminated in the future in favor of an @EnableShell annotation.  

With this setup all Spring CLI annotations should work as expected.