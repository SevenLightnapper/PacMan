# LR6
## Лабораторная работа №6
### Git

#### Певым шагом мы создаем копию [репозитория](https://github.com/Kurtyanik/LR6/) в личном хранилище.
![img.png](img.png)

#### Затем настраиваем клиент git, вводим имя пользователя и email. 
Комманды: `git config --global user.name <username>`; `git config --global user.email <email>`.
![img_1.png](img_1.png)

#### Клонируем удалённый репозиторий на компьютер. 
Комманда: `git clone <url>`.
![img_2.png](img_2.png)

#### Проверяем, что при клонировании репозитория, все файлы из удаленного репозитория перешли в локальный.
Команда: `git pull`.
![img_3.png](img_3.png)

#### Добавляем файл через интерфейс GitHub. 
- Нажимаем на кнопку <kbd>Add file</kbd>, затем на <kbd>Create new file</kbd>.
![img_4.png](img_4.png)
- С помощью появившегося интерфейса вводим название файла и его содержимое.
![img_5.png](img_5.png)
- Прописываем комментарий к коммиту и соответственно делаем сам коммит
![img_6.png](img_6.png)
- В итоге файл появляется в репозитории на GitHub
![img_7.png](img_7.png)

#### Поддтягиваем изменения с удаленного репозитория в локальный. 
Комманда: `git pull`.
![img_8.png](img_8.png)

#### Посмотрим коммиты веток 
* Коммиты ветки <b>*master*</b>. Комманда: `git log`.
![img_9.png](img_9.png)
* Переходим на вторую ветку <b>*branch1*</b>. Команда: `git checkout branch1`
![img_10.png](img_10.png)
* Коммиты ветки <b>*branch1*</b>. Комманда: `git log`.
![img_11.png](img_11.png)

#### Подробно рассмотрим коммиты ветки <b>*branch1*</b>. 
Комманда: `git log -p`.
![img_12.png](img_12.png)

#### Возвращаемся обратно на ветку <b>*master*</b>.
Команда: `git checkout master`.
![img_13.png](img_13.png)

#### Выполняем слияние ветки <b>*branch1*</b> в ветку <b>*master*</b>. 
Комманда: `git merge branch1`.
![img_14.png](img_14.png)

#### Разрешаем возникший конфликт: в теории конфликт возник из-за того, что файл mergefile.txt не отслеживается. Проверив это и убедившись, добавляем файл для отслеживания, составляем коммит. 
* Проверим, есть ли файлы, которые не отслеживаются. Команда: `git status`.
![img_15.png](img_15.png)
* Добавим файл для отслеживания. Команда: `git add mergefile.txt`.
![img_16.png](img_16.png)
  - Проверим, что больше не осталось неотслеживаемых файлов. Команда: `git status`.
![img_17.png](img_17.png)
* Создаем коммит. Команда: `git commit -m "merge conflict of branch1->master resolved"`.
![img_18.png](img_18.png)

#### Пушим (отправляем) наши локальные изменения в удаленный репозиторий.
Команды: `git status`; `git push`.
![img_22.png](img_22.png)

#### Удаляем (локально) побочную ветку после успешного слияния. 
* Удаляем ветку <b>*branch1*</b> локально. Команда: `git branch -d branch1`.
![img_19.png](img_19.png)
* Проверяем, что локальная ветка удалена. Команда: `git branch -a`
![img_20.png](img_20.png)

#### Добавляем (папку и файлы) изменения и фиксируем их (коммитим). Каждому изменения приписываем комментарий.
* Создаем папку и переходим в нее. Команды: `mkdir changes`; `cd changes/`.
![img_21.png](img_21.png)
* Создаем файл, соержащий в себе данные о папке. Команда: `echo "package LR6.changes;" > package-info.java`.
![img_23.png](img_23.png)
  - Добавляем файл в отслеживаемые. Команда: `git add package-info.java`.
![img_24.png](img_24.png)
  - Создаем коммит. Команда: `git commit -m "added new directory plus package-info"`.
![img_25.png](img_25.png)
* Создаем пустой файл. Команда: `echo "" > empty.txt`.
![img_26.png](img_26.png)
  - Добавляем файл в отслеживаемые. Команда: `git add empty.txt`.
![img_27.png](img_27.png)
  - Создаем коммит. Команда: `git commit -m "empty.txt added"`
![img_30.png](img_30.png)

#### Сделаем откат последнего коммита.
* Найдем идентификатоор последнего коммита. Команда: `git log`.
![img_28.png](img_28.png)
* Скопируем id коммита и откатим его. Команда: `git revert b37d5e4c839eb128f385dd1d74aaa2168648c422`.
![img_29.png](img_29.png)
![img_31.png](img_31.png)



#### Отправим (запушим) коммиты в удаленный репозиторий.
* Проверка состояния контроля версий. Команда: `git status`.
![img_32.png](img_32.png)
* Отправка коммитов. Команда: `git push`.
![img_33.png](img_33.png)
* Проверяем, что все запушили. Команда: `git status`.
![img_34.png](img_34.png)

#### Создаем ветку для отчёта. 
* Создаем ветку <b>*report*</b>. Комманда: `git branch report`.
![img_35.png](img_35.png)
* Переходим на ветку <b>*report*</b>. Команда: `git checkout report`.
![img_36.png](img_36.png)
* Создаем пустой файл отчета &mdash; <mark style="background-color: lightblue">README.md</mark>.
Команда: `echo "" > READEME.md`.
![img_37.png](img_37.png)
* Добавляем отчет в систему контроля версий. Команда: `git add README.md`.
![img_38.png](img_38.png)
* Создаем коммит с пустым отчетом. Команда: `git commit -m "added README.md empty report"`.
![img_39.png](img_39.png)

#### Получаем итоговую историю операций. 
Комманда: `git log`


Оформляем отчёт в файле <mark style="background-color: lightblue">README.md</mark>.
Среда разработки IntelliJ IDEA 2022.2 служит в данной работе редактором отчета.

После редактирования отчета, его нужно будет сохранить и закоммитить `git commit`.

В конце работы необходимо будет запушить все локальные изменения в сетевое хранилище GitHub командой `git push`.
