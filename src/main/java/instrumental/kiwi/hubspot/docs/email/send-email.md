```
{
"properties":{
        "hs_timestamp":"2019-10-30T03:30:17.883Z",
        "hubspot_owner_id":"47550177",
        "hs_email_direction":"EMAIL",
        "hs_email_status":"SENT",
        "hs_email_subject":"Let's talk",
        "hs_email_text":"Thanks for youremail",
        "hs_email_headers":"{\"from\":{\"email\":\"from@domain.com\",\"firstName\":\"FromFirst\",\"lastName\":\"FromLast\"},\"sender\":{\"email\":\"sender@domain.com\",\"firstName\":\"SenderFirst\",\"lastName\":\"SenderLast\"},\"to\":[{\"email\":\"ToFirst+ToLast<to@test.com>\",\"firstName\":\"ToFirst\",\"lastName\":\"ToLast\"}],\"cc\":[],\"bcc\":[]}"
    }
}
```

```
{
    "from": {
        "email": "from@domain.com",
        "firstName": "FromFirst",
        "lastName": "FromLast"
    },
    "to": [
        {
            "email": "ToFirst ToLast<to@test.com>",
            "firstName": "ToFirst",
            "lastName": "ToLast"
        }
    ],
    "cc": [],
    "bcc": []
}
```