# Launchpad Guide Prototype using ChatGPT

This is the backend project of the Launchpad Guide & provides two endpoints. 
1. [POST] /loadEmbeddings - to load launchpad functional data in CSV format, convert it into embeddings and store it in a CSV file.

2. [GET] /suggestions/count/{count}?text={userText} - to get relevant records based on user input query string
## Run Spring Boot application
Modify the following properties with local paths as applicable and then run the main class to star the program
```
openai.token={token}
output.csv.path={absolute path of output .csv file}
python.path={absolute path of Python3}
cosine.script.path={absolue path of cosineMatch.py}

```
## Sample CURL Commands

```

curl --location --request POST 'http://localhost:9090/gpt/loadEmbeddings' \
--form 'file=@"/Users/paulm2/Documents/input.csv"'

curl --location --request GET 'http://localhost:9090/gpt/suggestions/count/10?text=adsasd'

```
## Notes 
Sample input file given in files directory of root.
