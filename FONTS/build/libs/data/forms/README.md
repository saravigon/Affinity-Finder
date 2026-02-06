# FORMS STORAGE

This folder contains form data used to structure and manage user inputs. Each form is stored in its own file, named after the form's unique identifier.

### File name:
`<FormID>.json`

### Example structure:
```json
[
    {
        "title": "Encuesta de Satisfacción",
        "description": "Queremos conocer tu opinión sobre el servicio.",
        "questions": [
              {
                  "type": "OPEN_ENDED",
                  "text": "¿Qué opinas del producto?"
              },
              {
                  "type": "NUMERIC",
                  "text": "¿Qué nota le pondrías al producto?",
                  "min": 1,
                  "max": 5
              },
              {
                  "type": "MULTIPLE_CHOICE",
                  "text": "¿Cómo calificarías el servicio?",
                  "options": ["Excelente", "Bueno", "Regular", "Malo"],
                  "maxChoices" : 1
              }
        ]
    }
]
```