
<h1 align="center">
  <br>
 <img src="https://github.com/user-attachments/assets/b1eda41d-2c0f-45cb-839a-81b6ae4f0d2e" alt="" width="200" >
  <br>
  <span style="margin-top: 15px;">CS211 Project MyRequestKU</span>
  <br>
</h1>

<p align="center">
    <b>โปรเจกต์นี้เป็นส่วนหนึ่งของ รายวิชา 01418211 - การสร้างซอฟต์แวร์ (Software Construction)</b> <br>
    <b>ภาคการศึกษาที่ 1 ปีการศึกษา 2567</b> <br>
</p>

<p align="center">
<a href="https://git-scm.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="40" height="40"/> </a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/> </a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="40" height="40"/> </a>
&nbsp;&nbsp;&nbsp;&nbsp;
<a href="https://www.figma.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/figma/figma-icon.svg" alt="figma" width="40" height="40"/> </a> 
&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Showcase
![myrequestkuimg4](https://github.com/user-attachments/assets/9d21d55e-4dc7-4ab6-afa4-09f9497faa62)
![myrequestkuimg1](https://github.com/user-attachments/assets/b9d1858e-6209-420c-8dff-ee50b6e380e4)
![myrequestkuimg2](https://github.com/user-attachments/assets/8bfa4bb9-863e-42f2-adeb-046086ccc205)
![myrequestkuimg3](https://github.com/user-attachments/assets/405103a9-224c-4458-b7ea-0ff475579962)

## วิธีการติดตั้งและรันโปรแกรม
#### อธิบาย directory ของโปรเจกต์ที่เกี่ยวข้องกับวิธีการติดตั้งและรันโปรแกรม
1. data เก็บข้อมูล Mock-Data เริ่มต้น
2. release เก็บไฟล์ที่ใช้เปิดโปรแกรมทั้ง windows และ macos รวมถึง user-manual รูปแบบ PDF
3. release/data เก็บข้อมูล Mock-Data เริ่มต้นเหมือนกับ directory data
4. release/UML เก็บข้อมูล UML-Diagram ของคลาสภายในโปรเจกต์

#### อธิบายวิธีการติดตั้ง และวิธีการรันโปรแกรม
1. เลือก jar file ใน directory release ที่ตรงกับระบบปฏิบัติการของคุณ
2. หากต้องการข้อมูลเริ่มต้นเพื่อทดสอบให้นำ directory data (เก็บข้อมูลเริ่มต้น) ใน directory release มาไว้ใน directory เดียวกันกับ jar file
3. คลิกที่ jar file เพื่อเปิดใช้งานโปรแกรมได้เลย หรือใช้คำสั่ง java -jar ชื่อไฟล์.jar

![how-to-use](https://github.com/punsnx/project-extended-resources/blob/592e789c290ee15c6f2d78f6ab3ebe4937635c52/cs211-myrequestku-how-to-install-and-run-program.png)

## ตัวอย่างข้อมูลผู้ใช้ระบบ (username, password)
| บทบาท (role)       | ชื่อผู้ใช้ (username) | รหัสผ่าน (password) |
|--------------------| ---------------- | ----------------- |
| ผู้ดูแลระบบ        | admin         | adminSW211|
| เจ้าหน้าที่คณะ     | fscixxa     | faculty211 |
| เจ้าหน้าที่ภาควิชา | dcsxxa         | department211 |
| อาจารย์ที่ปรึกษา   | fsciusa            | advisor211 |
| นักเรียน           | b6610405905        | student211 |

## การวางโครงสร้างไฟล์ของโครงงาน
```markdown

├── data 
│   ├── images
│   │   └── users (เป็นโฟลเดอร์ที่เก็บข้อมูลรูปภาพของผู้ใช้)
│   ├── requests
│   │   └── signatures (เป็นโฟลเดอร์ที่เก็บข้อมูลไฟล์คำร้องที่ลงนามแล้ว)
│   └── users (เก็บข้อมูล csv ข้อมูลของผู้ใช้ทั้งหมดรวมทุกบทบาท)
└── src
    └── main
        ├── java
        │   └── ku
        │       └── cs
        │           ├── controllers
        │           │   ├── admin (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของบทบาทผู้ดูแลระบบ)
        │           │   ├── advisor (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของบทบาทอาจารย์ที่ปรึกษา)
        │           │   ├── department (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของบทบาทเจ้าหน้าที่ภาควิชา)
        │           │   ├── faculty (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของบทบาทเจ้าหน้าที่คณะ)
        │           │   ├── requests (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของคำร้อง)
        │           │   │   ├── approver (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ที่เกี่ยวข้องกับผู้อนุมัติคำร้อง)
        │           │   │   └── information (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่เกี่ยวข้องกับการแสดงข้อมูลคำร้องที่กรอกแล้ว)
        │           │   └── student (เป็นโฟลเดอร์ที่เก็บไฟล์ controllers ทั้งหมดที่ใช้ในระบบของบทบาทเจ้าหน้าที่นิสิต)
        │           ├── cs211671project (เป็นโฟลเดอร์ที่เก็บไฟล์ MainApplication)
        │           ├── models
        │           │   ├── department (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับภาควิชา)
        │           │   ├── faculty (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับคณะ)
        │           │   ├── request (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับคำร้อง)
        │           │   │   └── approver (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับผู้อนุมัติคำร้อง)
        │           │   │       └── exception (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับการตรวจสอบข้อผิดพลาดของผู้อนุมัติคำร้อง)
        │           │   └── user (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับผู้ใช้งาน)
        │           │       └── exceptions (เป็นโฟลเดอร์ที่เก็บไฟล์ models ทั้งหมดที่เกี่ยวข้องกับการตรวจสอบข้อผิดพลาดของผู้ใช้งาน)
        │           ├── services
        │           │   └── utils (เป็นโฟลเดอร์ที่เก็บไฟล์ services ทั้งหมดที่เกี่ยวข้องกับตัวช่วยจัดการข้อมูลต่างๆ)
        │           └── views (เก็บคลาสที่เกี่ยวกับการแสดงผลในโปรแกรม)
        │               ├── components (คลาสที่สืบทอดมาจาก JavaFx ที่เกี่ยวข้องกับการแสดงผล)
        │               └── layouts (คลาสรวมของ components เพื่อแสดงผลเฉพาะอย่าง)
        │                   ├── sidebar (คลาสที่เกี่ยวข้องกับ sidebar ที่ใช้เลือกนำทางไปยังส่วนต่างๆ)
        │                   └── theme (คลาสที่เกี่ยวข้องกับการใช้งานธีม)
        └── resources
            ├── fonts (เป็นโฟลเดอร์ที่เก็บฟอนต์ทั้งหมดที่ใช้ในระบบ)
            ├── images 
            │   ├── backgrounds (เป็นโฟลเดอร์ที่เก็บรูปภาพทั้งหมดที่ใช้เป็นพื้นหลังในหน้าต่างๆภายในระบบ)
            │   ├── icons (เป็นโฟลเดอร์ที่เก็บรูปภาพไอคอนทั้งหมดในระบบ) 
            │   ├── logos (เป็นโฟลเดอร์ที่เก็บรูปภาพ logo ของระบบ)
            │   ├── pages
            │   │   └── department (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชา)
            │   │       ├── department-staff-approver (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่เกี่ยวกับผู้อนุมัติระดับภาค)
            │   │       ├── department-staff-request (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่เกี่ยวกับคำร้อง)
            │   │       ├── global (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่แชร์กัน)
            │   │       │   └── components (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่แชร์กันเกี่ยวกับคลาสที่ใช้แสดงผล)
            │   │       │       └── textfield-stack (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่แชร์กันเกี่ยวกับช่องพิมพ์ข้อความ)
            │   │       └── sidebar (เป็นโฟลเดอร์ที่เก็บรูปภาพในหน้าของภาควิชาที่เกี่ยวกับส่วนนำทาง)
            │   ├── team-members (เป็นโฟลเดอร์ที่เก็บรูปภาพของสมาชิกภายในกลุ่ม runtime-error)
            └── ku
                └── cs
                    ├── styles (เป็นโฟลเดอร์ที่เก็บไฟล์ css ของโปรแกรม)
                    │   ├── department (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชา)
                    │   │   └── pages
                    │   │       ├── add-nisit (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าเพิ่มนิสิต)
                    │   │       ├── approver-list (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าแสดงผลผู้อนุมัติ)
                    │   │       ├── nisit-advisor-management (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าจัดการอาจารย์ที่ปรึกษา)
                    │   │       ├── nisit-management (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าจัดการนิสิต)
                    │   │       ├── request (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าจัดการคำร้อง)
                    │   │       └── request-list (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่เกี่ยวข้องกับภาควิชาในหน้าแสดงผลคำร้อง)
                    │   └── font (เป็นโฟลเดอร์ที่เก็บไฟล์ css ที่ใช้สำหรับการเปลี่ยนฟอนต์หรือเปลี่ยนขนาดฟอนต์ภายในระบบ)
                    └── views (เป็นโฟลเดอร์ที่เก็บไฟล์ fxml ทั้งหมดที่แสดงในระบบ)
```