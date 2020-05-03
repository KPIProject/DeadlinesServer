# Deadlines

## Зміст


### [Лог змін](#Log-change)

- [Версія 1.02](#1.02)

- [Версія 1.01](#1.01)


### [Запити на сервер](#API)

- [Юзер](#Юзер)  

	- [Реєстрація](#Реєстрація)  
	
	- [Логін](#Логін)  
	
	- [Всі проекти](#Всі-проекти)
	
	- [Деталі юзера](#Деталі-юзера)    
	
	- [Знайти юзерів по username](#Знайти-юзерів-по-username)
	
	- [Подивитись всі запрошення](#Подивитись-всі-запрошення)
	
	- [Прийняти запрошення](#Прийняти-запрошення)
	
	- [Відхилити запрошення](#Відхилити-запрошення)
	
	- [Редагувати юзера](#Редагувати-юзера)
	
	- [Видалити юзера](#Видалити-юзера)



- [Проект та дедлайн](#Проект-та-дедлайн)

	- [Створити проект](#Створити-проект)  

	- [Додати юзера у проект](#Додати-юзера-у-проект)  

	- [Додати дедлайн](#Додати-дедлайн)  

	- [Додати виконувача дедлайну](#Додати-виконувача-делайну)  

	- [Редагувати проект](#Редагувати-проект)  

	- [Редагувати дедлайн](#Редагувати-дедлайн)

	- [Видалити проект](#Видалити-проект)

	- [Видалити дедлайн](#Видалити-дедлайн)

	- [Видалити юзера з проекту](#Видалити-юзера-з-проекту)

	- [Видалити виконувача з дедлайну](#Видалити-виконувача-з-дедлайну)



- [Дебаг версії запитів](#Дебаг-версії-запитів)

	- [Створити проект дебаг](#Створити-проект-дебаг)

	- [Додати юзера у проект дебаг](#Додати-юзера-у-проект-дебаг)

	- [Всі юзери](#Всі-юзери)

  

- [Помилки](#Помилки)

	- [Список всіх помилок](#Список-всіх-помилок)



## Log change

### 1.02

#### Rename

- Повідомлення якщо сталася помилка або операція успішна

  Тепер мають таку структуру

  ```json
  {
      "type": "Exception",
      "code": 404,
      "message": "Unknown error"
  }
  ```

  `error_type` > `type`

  `error_message` > `message`

#### Edit

- Всі поля які приймали `uuid` для додавання юзерів у проекти та дедлайни теперь приймають `username`
- Вивід `uuid` тільки при логін та реєстрації
- Тепер при додавання когось у проект, юзер отримує запрошення, яке він может прийняти або відхилити

#### Add

- [Видалення юзерів](#Видалити-юзера) 
- [Видалення проектів](#Видалити-проект) 
- [Видалення дедлайнів](#Видалити-дедлайн) 
- [Видалення юзерів з проекту](Видалити-юзера-з-проекту)
- [Видалення виконувачів з дедлайну проекту](Видалити-юзера-з-проекту)
- [Видалення виконувачів з дедлайну проекту](Видалити-виконувача-з-делайну)
- [Зміна](#Редагувати-юзера) `username`, `userFirstName`, `userSecondName`, `password` y юзера
- [Зміна](#Редагувати-проект) `projectName`, `projectDescription`, `projectExecutionTime` y проекта
- [Зміна](#Редагувати-дедлайн) `deadlineId`, `deadlineName`, `deadlineDescription` у дедлайна
- [Запрошення у проект](#Подивитись-всі-запрошення)
- [Дебаг запити](#Дебаг версії запитів)



### 1.01

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
      "deadline": {
          "deadlineName": "My own deadline 1",
          "deadlineDescription": "Details of my deadline 1",
          "deadlineCreationTime": 123123123
      },
      "usersToAdd": []
  }
  ```

  
  - `deadlineExecutionTime`
  
    При створенні проекту = `0`
  
    **Або** можна вказати час закінчення дедлайну в `body`  [Додати делайн](#AddDeadline)
  
  ```json
  {
      "deadline": {
          "deadlineName": "My own deadline 1",
          "deadlineDescription": "Details of my deadline 1",
          "deadlineExecutionTime": 999999999
      },
      "usersToAdd": []
  }
  ```





## 	API

### Юзер

#### 	Реєстрація

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
    "type": "Error",
    "code": 404,
    "message": "User is already exist"
}
```

- Якщо `userFirstName` пустий або `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid userFirstName"
}
```

- Якщо `userSecondName` пустий або `null`  

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid userSecondName"
}
```

- Якщо `username` пустий або `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid username"
}
```

- Якщо `password` пустий або `null`  

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid password"
}
```



#### 	Логін

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
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Пароль для такого `username ` не правильний

```json
{
    "type": "Error",
    "code": 404,
    "message": "Password is wrong"
}
```

- Якщо `username` пустий або `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid username"
}
```

- Якщо `password` пустий або `null`  

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid password"
}
```



#### Всі проекти

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
        "projectId": 2,
        "projectName": "My own projecttttt2",
        "projectDescription": "Details of my project2",
        "deadlines": [
            {
                "deadlineId": 1,
                "deadlineName": "My own deadline 1",
                "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
                "deadlineProjectId": 2,
                "deadlineCreatedTime": 1588370977977,
                "deadlineExecutionTime": 999999999
            }
        ],
        "projectOwner": {
            "userId": 1,
            "userFirstName": "DenysDADADA",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk1",
            "userCreationTime": 1588370871083
         },
         "projectUsers": [
             {
                 "userId": 2,
                 "userFirstName": "Denys2",
                 "userSecondName": "Danilyuk2",
                 "username": "ddanilyuk2",
                 "userCreationTime": 1588370875757
             }
         ],
         "projectUsersInvited": [],
         "projectCreationTime": 123123123,
         "projectExecutionTime": 999999999
    }
]
```

​	*Список створених та до яких юзер приєднаний проектів (без запрошених)*

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### 	Деталі юзера

**URL**: `/{uuid}/details`

**Request type**: `GET`

**Variables**: 

```
uuid - uuid користувача
```

**Server successful answer**: 

```json
{
    "userId": 2,
    "userFirstName": "Denys2",
    "userSecondName": "Danilyuk2",
    "username": "ddanilyuk2",
    "userCreationTime": 1588370875757,
    "uuid": "a426ed57-3e59-4d5f-92d9-c2ba9a003f08"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### Знайти юзерів по username

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
        "username": "danisdanilyuk"
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
    "type": "Error",
    "code": 404,
    "message": "Users not found"
}
```



#### Подивитись всі запрошення

**URL**: `{uuid}/getInvitations`

**Request type**: `GET`

**Variables**: 

```
uuid - uuid користувача
```

**Server successful answer**: 

```json
[
    {
        "projectId": 13,
        "projectName": "My own projecttttt2",
   	    "projectDescription": "Details of my project2",
        "deadlines": [],
        "projectOwner": {
            "userId": 3,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk3",
            "userCreationTime": 1588413299112
        },
        "projectUsers": [],
        "projectUsersInvited": [
            {
                "userId": 2,
                "userFirstName": "Denys2",
                "userSecondName": "Danilyuk2",
                "username": "ddanilyuk2",
                "userCreationTime": 1588370875757
            }
        ],
        "projectCreationTime": 123123123,
        "projectExecutionTime": 999999999
    }
]
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### Прийняти запрошення

**URL**: `{uuid}/acceptInvite/{projectID}`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта
```

**Server successful answer**: 

```json
{
    "projectId": 13,
    "projectName": "My own projecttttt2",
    "projectDescription": "Details of my project2",
    "deadlines": [],
    "projectOwner": {
        "userId": 3,
        "userFirstName": "Denys2",
        "userSecondName": "Danilyuk2",
        "username": "ddanilyuk3",
        "userCreationTime": 1588413299112
    },
    "projectUsers": [
        {
            "userId": 2,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk2",
            "userCreationTime": 1588370875757
        }
    ],
    "projectUsersInvited": [],
    "projectCreationTime": 123123123,
    "projectExecutionTime": 999999999
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- Юзер не запрошений до цього проекту

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### Відхилити запрошення

**URL**: `{uuid}/rejectInvite/{projectID}`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Done"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- Юзер не запрошений до цього проекту

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### Редагувати юзера

**URL**: `{uuid}/editUser`

**Request type**: `POST`

**Variables**: 

```
uuid - uuid користувача
```

**Body**: 

```json
{
    "userFirstName": "Denis",
    "userSecondName": "Danilyuk",
    "username": "ddanilyukEdited",
    "password": "12345"
}
```

*Всі поля є опціональними!!!*

**Server successful answer**: 

```json
{
    "userId": 4,
    "userFirstName": "Denis",
    "userSecondName": "Danilyuk",
    "username": "ddanilyukEdited",
    "userCreationTime": 1588450838368,
    "projectsCreated": [],
    "projectsAppended": [],
    "projectsInvited": []
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```



#### Видалити юзера

**URL**: `{uuid}/deleteUser`

**Request type**: `DELETE`

**Variables**: 

```
uuid - uuid користувача
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Deleted"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```





### Проект та дедлайн

#### 	Створити проект

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
    "usersToAdd": ["ddanilyuk2"]
}
```

*`projectCreationTime` опціональне значення (якщо не вказати, впишеться автоматично)*

*`projectExecutionTime` опціональне значення (якщо не вказати, буде 0)*

*`usersToAdd: []` залиште пустий массив щоб не додавати юзерів до проекту*

**Server successful answer**: 

```json
{
    "projectId": 13,
    "projectName": "My own projecttttt2",
    "projectDescription": "Details of my project2",
    "deadlines": [],
    "projectOwner": {
        "userId": 3,
        "userFirstName": "Denys2",
        "userSecondName": "Danilyuk2",
        "username": "ddanilyuk3",
        "userCreationTime": 1588413299112
    },
    "projectUsersInvited": [
        {
            "userId": 2,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk2",
            "userCreationTime": 1588370875757
        }
    ],
    "projectUsers": [],
    "projectCreationTime": 123123123,
    "projectExecutionTime": 999999999
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Якщо `projectName` пустий або `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid projectName"
}
```

- Якщо `projectDescription == null`  

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid projectDescription"
}
```

ТАКОЖ ВСІ ПОМИЛКИ З `Додати юзера у проект` (якщо додати не існуючого користувача)*



#### 	Додати юзера у проект

**URL**: `{uuidOwner}/{projectID}/addUserToProject/{usernameToAdd}`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта
usernameToAdd - username юзера якого потрібно додати
```

**Server successful answer**: 

```json
{
    "projectId": 13,
    "projectName": "My own projecttttt2",
    "projectDescription": "Details of my project2",
    "deadlines": [],
    "projectOwner": {
        "userId": 3,
        "userFirstName": "Denys2",
        "userSecondName": "Danilyuk2",
        "username": "ddanilyuk3",
        "userCreationTime": 1588413299112
    },
    "projectUsersInvited": [
        {
            "userId": 2,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk2",
            "userCreationTime": 1588370875757
        }
    ],
    "projectUsers": [],
    "projectCreationTime": 123123123,
    "projectExecutionTime": 999999999
}
```

**Errors**:

- Користувача, якого ви збираєтесь додати, не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to add not found"
}
```

- Користувача, який керує цим проектом, не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User owner not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `userToAdd` вже в цьому проекті

```json
{
    "type": "Error",
    "code": 404,
    "message": "User is already in this project"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```

- Якщо запросити у проект самого себе

```json
{
    "type": "Error",
    "code": 404,
    "message": "User owner cant be invited to project"
}
```



#### Додати дедлайн

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
        "deadlineName": "My own deadline HELLO",
        "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
        "deadlineCreationTime": 123123123,
        "deadlineExecutionTime": 1231123552
    },
    "usersToAdd": ["ddanilyuk", "ddanilyuk3"]
}
```

`deadlineCreationTime` опціональне значення (якщо не вказати, впишеться автоматично)

`deadlineExecutionTime` опціональне значення (якщо не вказати, буде 0)

`usersToAdd: []` залиште пустий массив щоб не додавати юзерів до дедлайну

**Server successful answer**: 

```json
{
    "deadlineId": 6,
    "deadlineName": "My own deadline HELLO",
    "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
    "deadlineProjectId": 13,
    "deadlineExecutors": [
        {
            "userId": 2,
            "userFirstName": "Denis",
            "userSecondName": "Danilyuk",
            "username": "ddanilyuk",
            "userCreationTime": 1588370875757
        },
        {
            "userId": 3,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk3",
            "userCreationTime": 1588413299112
        }
    ],
    "deadlineCreatedTime": 1588456369847,
    "deadlineExecutionTime": 1231123552
}
```

**Errors**:

- Користувача (власника проекту) з таким `uuid ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- Якщо `deadlnineName` пустий або `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid deadlnineName"
}
```

- Якщо `deadlineDescription`  `null`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid deadlineDescription"
}
```

- Користувача (якого потрібно додати до дедлайну) з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to add not found"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Додати виконувача делайну

**URL**: `{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{usernameToAdd}`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта
deadlineId - id дедлайна до якого потрібно додати виконувача
usernameToAdd - username юзера якого потрібно додати
```

**Server successful answer**: 

```json
{
    "deadlineId": 6,
    "deadlineName": "My own deadline HELLO",
    "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
    "deadlineProjectId": 13,
    "deadlineExecutors": [
        {
            "userId": 2,
            "userFirstName": "Denis",
            "userSecondName": "Danilyuk",
            "username": "ddanilyuk",
            "userCreationTime": 1588370875757
        },
        {
            "userId": 3,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk3",
            "userCreationTime": 1588413299112
        }
    ],
    "deadlineCreatedTime": 1588456369847,
    "deadlineExecutionTime": 1231123552
}
```

**Errors**:

- Користувача якого ви збираєтесь додати не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to add not found"
}
```

- Користувача який керує цим проектом не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User owner not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- Дедлайн не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Deadline not found"
}
```

- `userToAdd` вже не доданий до цього проекту

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to add is not in this project"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Редагувати проект

**URL**: `{uuid}/{projectID}/editProject`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта який потрібно змінити
```

**Body**: 

```json
{
    "projectName": "My own projecttttt2",
    "projectDescription": "Details of my project 2",
    "projectExecutionTime": 999999999
}
```

*Всі поля є опціональними!!!*

**Server successful answer**: 

```json
{
    "projectId": 13,
    "projectName": "My own projecttttt2",
    "projectDescription": "Details of my project2",
    "deadlines": [],
    "projectOwner": {
        "userId": 3,
        "userFirstName": "Denys2",
        "userSecondName": "Danilyuk2",
        "username": "ddanilyuk3",
        "userCreationTime": 1588413299112
    },
    "projectUsersInvited": [
        {
            "userId": 2,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk2",
            "userCreationTime": 1588370875757
        }
    ],
    "projectUsers": [],
    "projectCreationTime": 123123123,
    "projectExecutionTime": 999999999
} 
```

**Errors**:

- Користувача який керує цим проектом не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Редагувати дедлайн

**URL**: `{uuid}/{projectID}/{deadlineID}/editDeadline`

**Request type**: `POST`

**Variables**: 

```
uuidOwner - uuid власника проекта
projectID - id проекта
deadlineID - id дедлайна який потрібно змінити
```

**Body**: 

```json
{
    "deadlineName": "My own deadline EDITED",
    "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
    "deadlineExecutionTime": 1231123552
}
```

*Всі поля є опціональними!!!*

**Server successful answer**: 

```json
{
    "deadlineId": 6,
    "deadlineName": "My own deadline EDITED",
    "deadlineDescription": "Details of my deadline 1Details of my deadline 1D",
    "deadlineProjectId": 13,
    "deadlineExecutors": [
        {
            "userId": 2,
            "userFirstName": "Denis",
            "userSecondName": "Danilyuk",
            "username": "ddanilyuk",
            "userCreationTime": 1588370875757
        },
        {
            "userId": 3,
            "userFirstName": "Denys2",
            "userSecondName": "Danilyuk2",
            "username": "ddanilyuk3",
            "userCreationTime": 1588413299112
        }
    ],
    "deadlineCreatedTime": 1588456369847,
    "deadlineExecutionTime": 1231123552
}
```

**Errors**:

- Користувача який керує цим проектом не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проект не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- Дедлайн не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Deadline not found"
}
```

- `userOwner` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Видалити проект

**URL**: `{uuid}/{projectID}/deleteProject`

**Request type**: `DELETE`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта який необхідно видалити
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Deleted"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проекту з таким `id ` не знайдено

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `user` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Видалити дедлайн

**URL**: `{uuid}/{projectID}/{deadlineID}/deleteDeadline`

**Request type**: `DELETE`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта з якого необхідно видалити дедлайн
deadlineID - id дедлайна який необхідно видалити
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Deleted"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Проекту з таким `id ` не знайдено

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `user` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```

- Дедлайн не знайдений

```json
{
    "type": "Error",
    "code": 404,
    "message": "Deadline not found"
}
```

- Дедлайн не знаходиться у цьому проекті

```json
{
    "type": "Error",
    "code": 404,
    "message": "Deadline is not in this project"
}
```



#### Видалити юзера з проекту

**URL**: `{uuid}/{projectID}/deleteUserFromProject/{usernameToDelete}`

**Request type**: `DELETE`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта з якого необхідно видалити юзера
usernameToDelete - username юзера якого необхідно видалити
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Deleted"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Користувача з таким `username ` якого ви хочете видалити не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to delete not found"
}
```

- Проекту з таким `projectID ` не знайдено

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `user` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```



#### Видалити виконувача з дедлайну

**URL**: `{uuid}/{projectID}/{deadlineID}/deleteExecutorFromDeadline/{usernameToDelete}`

**Request type**: `DELETE`

**Variables**: 

```
uuid - uuid користувача
projectID - id проекта в якому знаходиться дедлайн
deadlineID - id дедлайна з якого необхідно видалити юзера
usernameToDelete - username юзера якого необхідно видалити
```

**Server successful answer**: 

```json
{
    "type": "Success",
    "code": 200,
    "message": "Deleted"
}
```

**Errors**:

- Користувача з таким `username ` не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User not found"
}
```

- Користувача з таким `username ` якого ви хочете видалити не існує

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to delete not found"
}
```

- Проекту з таким `projectID ` не знайдено

```json
{
    "type": "Error",
    "code": 404,
    "message": "Project not found"
}
```

- `user` не керуює проектом з цим `projectID`

```json
{
    "type": "Error",
    "code": 404,
    "message": "Invalid project owner"
}
```

- `user` якого ви хочете видалити не знаходиться в цьому проекті

```json
{
    "type": "Error",
    "code": 404,
    "message": "User to delete is not this project"
}
```

- Дедлайн з якого ви хочете видалити виконувача не знаходиться в цьому проекті

```json
{
    "type": "Error",
    "code": 404,
    "message": "Deadline is not in this project"
}
```





### Дебаг версії запитів

#### Створити проект дебаг

Дебаг версія запиту [створити проект](#Створити-проект), але юзери яких ви хочете додати до проекту одразу попадають у проект, минуючи запрошення.

**URL**: `{uuid}/createProjectDebug`



#### Додати юзера у проект дебаг

Дебаг версія запиту [додати юзера у проект](#Додати-юзера-у-проект), але юзери яких ви хочете додати до проекту одразу попадають у проект, минуючи запрошення.

**URL**: `{uuidOwner}/{projectID}/addUserToProjectDebug/{usernameToAdd}`



#### Всі юзери

Запит для отримання списку всіх юзерів (з uuid та проектами)

**URL**: `/all`

**Request type**: `GET`





### Помилки

#### Список всіх помилок

SOON









Модель роботи проекта:

1. Серверна частина на Java c RestApi и БД SQL
2. Веб-сайт
3. IOS Додаток



## Веб-сайт

...



## IOS

https://github.com/ipzProject/DeadlinesManager
