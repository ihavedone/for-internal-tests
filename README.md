# Hardware Security Module Stub

### About

**Test repository to check detection mechanisms.** Fake repository with some leaks, e.g. to automate CVV calculation process.


### How to use

POST http://localhost:8080/internal/api/card/cvv
Content-Type: application/json

{
"pan": "****************"
}



### Nota Bene

This is a fake repository. All keys (in application.yml file) and other data (especially method of CVV calculation) are fictional and does not contain anything in common with the real world.
