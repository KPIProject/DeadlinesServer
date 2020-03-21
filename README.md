# Deadlines

Модель роботи проекта:

1. Серверная частина на Java c RestApi и БД SQL
2. Веб-сайт
3. IOS Додаток

## Log change

### Version 1.01

#### Rename

-  `projectAciveUsersUuid`  to `projectUsersUuid`

#### Edit

- `projectDescription` кількість символів 8192
- Структура запиту на створення проекту [Створити проект](#CreateProject)
- Структура запиту на додання дедлайну  [Додати делайн](#AddDeadline)

#### Add

- `userCreationTime` to `User`

  При реєстрації автоматично створюється час

- `projectCreationTime`, `projectExecutionTime` to `Project` 

  - `projectCreationTime`

    Створюється автоматично при створенні проекта

    **Або** можна вказати час створення в `body`  [Створити проект](#CreateProject)

  ```json
  {
   "project": {
    "projectName": "My own project",
    "projectDescription": "Details of my project",
    "projectCreationTime": 123123123
   },
   "usersToAdd": []
  }
  ```

  - `projectExecutionTime`

    При створенні проекту = `0` без заначеного `projectExecutionTime`

    **Або** можна вказати час закінчення проекту в `body`  [Створити проект](#CreateProject)

  ```json
  {
   "project": {
    "projectName": "My own project",
    "projectDescription": "Details of my project",
    "projectExecutionTime": 123123123
   },
   "usersToAdd": []
  }
  ```

- `deadlineCreationTime`, `deadlineExecutionTime` to `Deadline` 

  - `deadlineCreationTime`

    Створюється автоматично при створенні дедлайна

    **Або** можна вказати час створення в `body`  [Додати делайн](#AddDeadline)

  ```json
  {
  	"deadlineName": "My own deadline",
  	"deadlineDescription": "Details of my deadline",
  	"deadlineCreationTime": 158479510000
  }
  ```

  - `deadlineExecutionTime`

    При створенні проекту = `0`

    **Або** можна вказати час закінчення дедлайну в `body`  [Додати делайн](#AddDeadline)

  ```json
  {
  	"deadlineName": "My own deadline",
  	"deadlineDescription": "Details of my deadline",
  	"deadlineExecutionTime": 158479599998
  }
  ```





## 	Працюючі запити

[Регєстрація](#Registration)  

[Логін](#Login)  

[Всі проекти](#AllProjects)  

[Знайти юзерів по username](#FindByUsername)

[Деталі юзера](#UserDatail)  

[Створити проект](#CreateProject)  

[Додати юзера до проекту](#AddUserToProject)  

[Додати дедлайн](#AddDeadline)  

[Додати виконувача проекту](#AddExecutorToDeadline)  

[Деталі дедлайну](#DeadlineDetail)  

[Деталі проекту](#ProjectDetail)



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
  "projectsAppended": [],
  "userCreationTime": 1584818029554
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
  "projectsAppended": [],
  "userCreationTime": 1584818029554
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



### 	AllProjects

**URL**: `{uuid}/allProjects`

**Request type**: `GET`

**Variables**: 

```
uuid - uuid користувача
```

**Server successful answer**: 

```json
[
  {
    "projectId": 1,
    "projectName": "My own project",
    "projectDescription": "Details of my project",
    "deadlines": [],
    "projectOwner": {
      "userId": 1,
      "userFirstName": "Denys",
      "userSecondName": "Danilyuk",
      "username": "danisdanilyuk",
      "uuid": "7027a4eb-b409-4df8-b4be-725f0faa3a05",
  		"userCreationTime": 1584818029554
    },
    "projectUsers": []
  }
]
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



### 	UserDetail

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



### 	CreateProject

**URL**: `{uuid}/createProject`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid користувача
```

**Body**: 

```json
{
 "project": {
  "projectName": "My own project 2",
  "projectDescription": "Details of my project 2",
  "projectCreationTime": 123123123,
  "projectExecutionTime": 999999999
 },
 "usersToAdd": ["1bcfbad3-31d7-4e48-936e-461e0d2a445c"]
}
```

*`projectCreationTime` опціональне значення (якщо не вказати, впишеться автоматично)*

*`projectExecutionTime` опціональне значення (якщо не вказати, буде 0)*

*`usersToAdd: []` залиште пустий массив щоб не додавати юзерів до проекту*

**Server successful answer**: 

```json
{
  "projectId": 1,
  "projectName": "My own project 2",
  "projectDescription": "Details of my project 2",
  "deadlines": [],
  "projectOwner": {
    "userId": 1,
    "userFirstName": "Denys2",
    "userSecondName": "Danilyuk2",
    "username": "ddanilyuk3",
    "uuid": "197f6109-2973-43a5-9c22-2888fc813838",
    "userCreationTime": 1584818897341
  },
  "projectUsers": [
    {
      "userId": 2,
      "userFirstName": "Denys",
      "userSecondName": "Danilyuk",
      "username": "ddanilyuk",
      "uuid": "1bcfbad3-31d7-4e48-936e-461e0d2a445c",
      "userCreationTime": 1584818933775
    }
  ],
  "projectCreationTime": 123123123,
  "projectExecutionTime": 999999999
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

*ТАКОЖ ВСІ ПОМИЛКИ З `AddUserToProject` (якщо додати не існуючого користувача)*

### 	AddUserToProject

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



### AddDeadline

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
 "deadline": {
  "deadlineName": "My own deadline 1",
  "deadlineDescription": "Details of my deadline 1",
  "deadlineCreationTime": 123123123,
  "deadlineExecutionTime": 999999999
 },
 "usersToAdd": ["47efeb77-7635-405c-bd21-dfbc60fe1dae"]
}
```

`deadlineCreationTime` опціональне значення (якщо не вказати, впишеться автоматично)

`deadlineExecutionTime` опціональне значення (якщо не вказати, буде 0)

`usersToAdd: []` залиште пустий массив щоб не додавати юзерів до дедлайну

**Server successful answer**: 

```json
{
  "projectId": 1,
  "projectName": "My own project",
  "projectDescription": "Details of my project",
  "deadlines": [
    {
      "deadlineId": 1,
      "deadlineName": "My own deadline 1",
      "deadlineDescription": "Details of my deadline 1",
      "deadlineProjectId": 1,
      "deadlineExecutorsUuid": [
        "47efeb77-7635-405c-bd21-dfbc60fe1dae"
      ],
      "deadlineCreatedTime": 1584819730624,
      "deadlineExecutionTime": 999999999
    }
  ],
  "projectOwner": {
    "userId": 1,
    "userFirstName": "Denys",
    "userSecondName": "Danilyuk",
    "username": "ddanilyuk",
    "uuid": "c289cb0f-f752-42af-9b68-3d21d3ca39b0",
    "userCreationTime": 1584819579859
  },
  "projectUsers": [
    {
      "userId": 2,
      "userFirstName": "Denys2",
      "userSecondName": "Danilyuk2",
      "username": "ddanilyuk2",
      "uuid": "47efeb77-7635-405c-bd21-dfbc60fe1dae",
      "userCreationTime": 1584819588258
    }
  ],
  "projectCreationTime": 123123123,
  "projectExecutionTime": 999999999
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



### AddExecutorToDeadline

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



### DeadlineDetail

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



### FindByUsername

**URL**: `findByUsername/{username}`

**Request type**: `GET`

**Variables**: 

```
username - юзернейм
```

**Server successful answer**: 

```json
[
    {
        "userId": 1,
        "userFirstName": "Denys",
        "userSecondName": "Danilyuk",
        "username": "danisdanilyuk",
        "uuid": "7027a4eb-b409-4df8-b4be-725f0faa3a05"
    }
]
```

```
Якщо ввести username не повністю, то повертає список юзерів зі схожим username
```

**Errors**:

- Коли юзерів по данному username немає

```json
{
  "error_type": "NotFoundException",
  "code": 404,
  "error_message": "Users not found"
}
```



## Веб-сайт

...



## IOS

https://github.com/ipzProject/DeadlinesManager
