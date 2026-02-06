# ANSWERS STORAGE

This directory contains the answers given by the users in the application. Each answer is stored in a separate file, and the files are named according to a combination of the user's ID and the form ID to ensure uniqueness.

### File name:
`<FormID>_<UserID>.json`

### Example structure:
```json
[
    {
        "creator": "user123",
        "formId": "form456",
        "questions": [
            {
                "questionText": "¿Qué opinas del producto?",
                "answer": "Me parece excelente y muy útil."
            },
            {
                "questionText": "¿Qué nota le pondrías al producto?",
                "answer": 5
            },
            {
                "questionText": "¿Cómo calificarías el servicio?",
                "answer": ["Excelente"]
            }
        ]
    }
]
```