# server info
##payload for click-request

URL: http://poll.doganyazar.com:8001/

POST /app/click
```
{
  value: number
  id: uuid
}
```
### example
```
{ 
  value:1,
  id:'64f3bd7a-e53b-44fa-a3ff-41a0a62cc78c'
}
```

## get questions from server
GET /app/questions/
```
[
  {"_id":"26fa5caa-c640-42e8-b146-4fab7eb68454",
    "header":"Sexy Question",
    "text":"I am a very sexy question",
    "id":1464032572638
  }
]
```
returns an array of questions.
ignore id, use _id for now.
