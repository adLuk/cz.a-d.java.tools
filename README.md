# cz.a-d.java.tools

Repository with source code of utils class handful for collections and other common data structure.

# Main project structure

Project is assembled from multiple small modules producing separate jar files for every utils class, to allow import
just classes you are actually need in your project.

# Tech stack of project

Project is using multi-module gradle configuration to provide structure of libraries produced from single repository.
Implementation is done in latest java (currently java 23). 

## signing configuration
To sign jars published by project, gradle is configured to use gpg command. To finish configuration in local environment
or in integration pipeline to be able to sign published libraries. You need to add gradle.properties file in location 
where gradle can locate it, preferably outside of repository to prevent unwanted commit of your secret configuration 
into repository. 
- signing.gnupg.executable=gpg
- signing.gnupg.useLegacyGpg=false
- signing.gnupg.homeDir=/home/john/.gnupg
- signing.gnupg.keyName="Name of key"
- signing.gnupg.passphrase="password used to protect key"

- executable The gpg executable that is invoked for signing. The default value of this property depends on useLegacyGpg. 
If that is true then the default value of executable is "gpg" otherwise it is "gpg2".
- useLegacyGpg Must be true if GnuPG version 1 is used and false otherwise. The default value of the property is false.
- homeDir Sets the home directory for GnuPG. If not given the default home directory of GnuPG is used.
- keyName The id of the key that should be used for signing. If not given then the default key configured in GnuPG will
be used.
- passphrase The passphrase for unlocking the secret key. If not given then the gpg-agent program is used for getting
the passphrase.

