# Deadlines

Модель роботи проекта:

1. Серверная частина на Java c RestApi и БД SQL
2. Веб-сайт
3. IOS Додаток



## 	Працюючі запити

### 	Registration

**URL**: `/registration`

**Request type**: `POST`

**Body**: 

```json
{
	"userFirstName": "Denis",
	"userSecondName": "Danilyuk",
	"username": "ddanilyuk",
	"password": "12345"
}
```

**Server successful answer**: 

```json
{
  "userId": 4,
  "userFirstName": "Denis",
  "userSecondName": "Danilyuk",
  "username": "ddanilyuk",
  "uuid": "ddd76d87-ae08-4f4d-b939-df09720b2479",
  "projectsCreated": [],
  "projectsAppended": []
}
```

​	*Uuid користувача буде використовуватися для всіх інших операцій!*

**Errors**:

- Користувач з таким `username ` уже існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User is already exist"
}
```

- Якщо `userFirstName` пустий або `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid userFirstName"
}
```

- Якщо `userSecondName` пустий або `null`  

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid userSecondName"
}
```

- Якщо `username` пустий або `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid username"
}
```

- Якщо `password` пустий або `null`  

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid password"
}
```



### 	Login

**URL**: `/login`

**Request type**: `POST`

**Body**: 

```json
{
	"username": "ddanilyuk",
	"password": "12345"
}
```

**Server successful answer**: 

```json
{
  "userId": 4,
  "userFirstName": "Denis",
  "userSecondName": "Danilyuk",
  "username": "ddanilyuk",
  "uuid": "ddd76d87-ae08-4f4d-b939-df09720b2479",
  "projectsCreated": [],
  "projectsAppended": []
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User not found"
}
```

- Пароль для такого `username ` не правильний

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Password is wrong"
}
```

- Якщо `username` пустий або `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid username"
}
```

- Якщо `password` пустий або `null`  

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid password"
}
```



### 	User detail

**URL**: `/{uuid}/details`

**Request type**: `GET`

**Variables**: 

```
uuid - uuid користувача
```

**Server successful answer**: 

```json
{
  "userId": 5,
  "userFirstName": "Nastya",
  "userSecondName": "Holovash",
  "username": "username",
  "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226",
  "projectsCreated": [
    {
      "projectId": 25,
      "projectName": "name project",
      "projectDescription": "project description",
      "deadlines": [],
      "projectOwnerUuid": "3c8e6d64-423d-4b63-a162-ab46a979f226",
      "projectActiveUsersUuid": []
    }
  ],
  "projectsAppended": [
    {
      "projectId": 26,
      "projectName": "Denys project",
      "projectDescription": "Details of denys project",
      "deadlines": [],
      "projectOwnerUuid": "982b13ac-b2bc-40a0-a7a3-563d801e4e50",
      "projectActiveUsersUuid": [
        "3c8e6d64-423d-4b63-a162-ab46a979f226"
      ]
    }
  ]
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User not found"
}
```



### 	Create project

**URL**: `{uuid}/createProject`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid користувача
```

**Body**: 

```json
{
	"projectName": "name project",
	"projectDescription": "project description"
}
```

**Server successful answer**: 

```json
{
  "projectId": 25,
  "projectName": "name project",
  "projectDescription": "project description",
  "deadlines": [],
  "projectOwner": {
    "userId": 5,
    "userFirstName": "Nastya",
    "userSecondName": "Holovash",
    "username": "username",
    "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
  },
  "projectUsers": []
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User not found"
}
```

- Якщо `projectName` пустий або `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid projectName"
}
```

- Якщо `projectDescription == null`  

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid projectDescription"
}
```



### 	Add user to project

**URL**: `{uuidOwner}/{projectID}/addUserToProject/{uuidUserToAdd}`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта
uuidUserToAdd - uuid юзера якого потрібно додати
```

**Server successful answer**: 

```json
{
  "projectId": 26,
  "projectName": "Denys project",
  "projectDescription": "Details of denys project",
  "deadlines": [],
  "projectOwner": {
    "userId": 6,
    "userFirstName": "Denys",
    "userSecondName": "Danilyuk",
    "username": "danisdanilyuk",
    "uuid": "982b13ac-b2bc-40a0-a7a3-563d801e4e50"
  },
  "projectUsers": [
    {
      "userId": 5,
      "userFirstName": "Nastya",
      "userSecondName": "Holovash",
      "username": "username",
      "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
    }
  ]
}
```

**Errors**:

- Користувача якого ви збираєтесь додати не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User to add not found"
}
```

- Користувача який керує цим проектом не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User owner not found"
}
```

- Проект не знайдений

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Project not found"
}
```

- `userToAdd` вже в цьому проекті

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User is already in this project"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid project owner"
}
```



### Add deadline

**URL**: `/{uuid}/{projectID}/addDeadline`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid власника проекта
projectID - id проекта
```

**Body**: 

```json
{
	"deadlineName": "Denys deadline",
	"deadlineDescription": "Details of denys deadline"
}
```

**Server successful answer**: 

```json
{
  "projectId": 26,
  "projectName": "Denys project",
  "projectDescription": "Details of denys project",
  "deadlines": [
    {
      "deadlineId": 4,
      "deadlineName": "Denys deadline",
      "deadlineDescription": "Details of denys deadline",
      "deadlineProjectId": 26,
      "deadlineExecutorsUuid": []
    }
  ],
  "projectOwner": {
    "userId": 6,
    "userFirstName": "Denys",
    "userSecondName": "Danilyuk",
    "username": "danisdanilyuk",
    "uuid": "982b13ac-b2bc-40a0-a7a3-563d801e4e50"
  },
  "projectUsers": [
    {
      "userId": 5,
      "userFirstName": "Nastya",
      "userSecondName": "Holovash",
      "username": "username",
      "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
    }
  ]
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User not found"
}
```

- Якщо `deadlnineName` пустий або `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid deadlnineName"
}
```

- Якщо `deadlineDescription`  `null`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid deadlineDescription"
}
```



### Add executor to deadline

**URL**: `{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{uuidUserToAdd}`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта
deadlineId - id дедлайна до якого потрібно додати виконувача проекта
uuidUserToAdd - uuid юзера якого потрібно додати
```

**Server successful answer**: 

```json
{
  "projectId": 26,
  "projectName": "Denys project",
  "projectDescription": "Details of denys project",
  "deadlines": [
    {
      "deadlineId": 4,
      "deadlineName": "Denys deadline",
      "deadlineDescription": "Details of denys deadline",
      "deadlineProjectId": 26,
      "deadlineExecutorsUuid": [
        "3c8e6d64-423d-4b63-a162-ab46a979f226"
      ]
    }
  ],
  "projectOwner": {
    "userId": 6,
    "userFirstName": "Denys",
    "userSecondName": "Danilyuk",
    "username": "danisdanilyuk",
    "uuid": "982b13ac-b2bc-40a0-a7a3-563d801e4e50"
  },
  "projectUsers": [
    {
      "userId": 5,
      "userFirstName": "Nastya",
      "userSecondName": "Holovash",
      "username": "username",
      "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
    }
  ]
}
```

**Errors**:

- Користувача якого ви збираєтесь додати не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User to add not found"
}
```

- Користувача який керує цим проектом не існує

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User owner not found"
}
```

- Проект не знайдений

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Project not found"
}
```

- Дедлайн не знайдений

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Deadline not found"
}
```

- `userToAdd` вже не доданий до цього проекту

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "User to add is not in this project"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Invalid project owner"
}
```



### Deadline detail

**URL**: `deadlineDetail/{id}`

**Request type**: `GET`

**Variables**: 

```
id - id дедлайна
```

**Server successful answer**: 

```json
{
  "deadlineId": 4,
  "deadlineName": "Denys deadline",
  "deadlineDescription": "Details of denys deadline",
  "deadlineProjectId": 26,
  "deadlineExecutorsUuid": [
    "3c8e6d64-423d-4b63-a162-ab46a979f226"
  ],
  "deadlineExecutors": [
    {
      "userId": 5,
      "userFirstName": "Nastya",
      "userSecondName": "Holovash",
      "username": "username",
      "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
    }
  ]
}
```

**Errors**:

- Дедлайн не знайден

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Deadline not found"
}
```



### Project detail

**URL**: `projectDetail/{id}`

**Request type**: `GET`

**Variables**: 

```
id - id проекта
```

**Server successful answer**: 

```json
{
  "projectId": 26,
  "projectName": "Denys project",
  "projectDescription": "Details of denys project",
  "deadlines": [
    {
      "deadlineId": 4,
      "deadlineName": "Denys deadline",
      "deadlineDescription": "Details of denys deadline",
      "deadlineProjectId": 26,
      "deadlineExecutorsUuid": [
        "3c8e6d64-423d-4b63-a162-ab46a979f226"
      ],
      "deadlineExecutors": [
        {
          "userId": 5,
          "userFirstName": "Nastya",
          "userSecondName": "Holovash",
          "username": "username",
          "uuid": "3c8e6d64-423d-4b63-a162-ab46a979f226"
        }
      ]
    }
  ]
}
```

**Errors**:

- Проект не знайден

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Project not found"
}
```





## Веб-сайт

...



## IOS

...
