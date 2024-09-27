# CS211 Project ภาคต้น 2567

## ชื่อทีม runtime-error

### สมาชิกในทีม
| รหัสนิสิต  | ชื่อ-นามสกุล (ชื่อเล่น)    | GitHub username |
|------------|----------------------------|-----------------|
| 6610402078 | ธนอนันท์ เฉลิมพันธ์ (เอ็ม) | Tanaanan        |
| 6610402205 | รักษิต รุ่งรัตนไชย (เนสท์) | Rugsit          |
| 6610402230 | ศิริสุข ทานธรรม (ข้าวปั้น) | punsnx          |
| 6610405905 | นรากร ธนาพรภักดี (น้ำ)     | nk-n            |

## คลิปความก้าวหน้าของระบบ
| ครั้งที่                      |       กำหนดส่ง        | Youtube Link                 |
|-------------------------------|:---------------------:|------------------------------|
| ความก้าวหน้าของระบบครั้งที่ 1 | 9 ส.ค. 2567 17:00 น.  | https://youtu.be/guBQMM8nSbE |
| ความก้าวหน้าของระบบครั้งที่ 2 | 6 ก.ย. 2567 17:00 น.  | https://youtu.be/nyvjvJVy8Pg |
| ความก้าวหน้าของระบบครั้งที่ 3 | 27 ก.ย. 2567 17:00 น. | https://youtu.be/84YbGZsQRcY                 |
| โครงงานที่สมบูรณ์             | 18 ต.ค. 2567 17:00 น. | ____________                 |

## สรุปสิ่งที่พัฒนาในแต่ละครั้ง
### ความก้าวหน้าของระบบครั้งที่ 1
1. ธนอนันท์ เฉลิมพันธ์ (เอ็ม)
   * สร้างหน้าจัดการนิสิตในที่ปรึกษา และจัดการคำร้องที่รอการอนุมัติ
   * วางโครงสร้างในส่วนของ Table view หลักที่จะใช้แสดงผลรายชื่อนิสิต และข้อมูลคำร้อง
2. รักษิต รุ่งรัตนไชย (เนสท์)
   * ออกแบบ user interface ของหน้า login, register, admin, user p rofile
   * นำ user interface ที่ได้ทำการออกแบบไว้ทั้ง 4 หน้ามา implement เป็น fxml ด้วย scene builder และ css style
   * สร้าง method ในการเชื่อมโยงภายในหน้าของ admin และลิงก์ไปยังหน้า user profile ด้วย controller
   * ทำระบบ debug เบื้องต้นสำหรับหน้า login ในกรณีที่ยังไม่สามารถเช็กข้อมูลที่กรอกกับ data ในไฟล์ได้เพื่อให้สามารถเข้าไปยังหน้าต่างของ role ต่างๆได้
3. ศิริสุข ทานธรรม (ข้าวปั้น)
   * (1.) สร้างโครงสร้างหน้าพื้นฐานในแต่ละหน้า
   * (2.) สร้าง class components หลักๆที่มีการเรียกใช้ เช่น label, button, image
     ทำให้ลดความซ้ำซ้อนของโค้ดและ ง่ายต่อการจัดการ และมี derived class ที่เกี่ยวข้องกับ feature
     ของ components - e.g. มี route button, square image
   * (3.) สร้าง method ในการ crop image ทั้งแบบ aspect ratio และ square
   * (4.) สร้าง method ในการ เช็ก fonts ว่ามีอยู่ใน system และ fallbackFont
   * (5.) สร้าง method ในการ load fonts เข้า system fonts list สำหรับใช้ใน project
4. นรากร ธนาพรภักดี (น้ำ)
   * ออกแบบ GUI ด้วย figma ในส่วนของหน้าต่าง ๆ ที่เกี่ยวกับ role นิสิต, หน้าแสดงข้อมูลใบคำร้อง, และหน้าที่แสดงสมาชิกในทีมรวมถึงวิธีการใช้ และคำแนะนำสำหรับโปรแกรม
   * ทำ GUI สำหรับหน้า "คำร้องทั้งหมด" และ "โปรไฟล์" สำหรับ role นิสิตที่ค่อนข้างตรงกับที่ออกแบบไว้ และสามารถเชื่อมถึงกันได้ ด้วยการทำ fxml, css, และ controller
   * แก้ไข css ใหม่ (alignment, font weight, font size, etc.) หลังจากทำการเปลี่ยน font ของโปรแกรม แล้วเจอปัญหาด้านการแสดงผล
   * เพิ่ม method ที่เชื่อมกับ label "ออกจากระบบ" ให้กับเกือบทุกหน้า เพื่อให้สามารถกลับไปหน้า login ได้ ช่วยให้ทีมสามารถเข้าถึงทุก ๆ หน้าได้โดยไม่ต้อง run ใหม่

### ความก้าวหน้าของระบบครั้งที่ 2
1. ธนอนันท์ เฉลิมพันธ์ (เอ็ม)
   * สร้างหน้า UI เพิ่มในส่วนของเจ้าหน้าที่คณะ
   * สร้างหน้าระบบ Authentication ใช้ในการ login จาก datasource ที่มีอยู่แล้ว และระบบ register ให้นิสิตลงทะเบียนหลังจากที่เจ้าหน้าที่ภาควิชาได้ทำการเพิ่มข้อมูลนิสิตในระบบเป็นที่เรียบร้อยแล้ว
   * เพิ่ม Authentication, LoginController และ RegisterController
2. รักษิต รุ่งรัตนไชย (เนสท์)
   * ออกแบบหน้า GUI ของคำร้องทั้งหมด ประกอบด้วย หน้าคำร้องทั่วไป, หน้าคำร้องขอลงทะเบียน, คำร้อง KU1, คำร้อง KU3
   * เขียน controller ของหน้าคำร้องทั้งหมดเพื่อ ดึงเอาข้อมูลที่ผู้ใช้กรอกในฟอร์ม มาสร้างคลาสใบคำร้อง เพื่อเตรียมเขียนลงไฟล์ และดักจับ error ที่โยนมาจาก class คำร้องแต่ละแบบเพื่อแสดง dialog แจ้งเตือนผู้ใช้
   * เขียนและออกแบบ model class คำร้อง ทั้งหมดได้แก่ class คำร้องทั่วไป, class คำร้องขอลงทะเบียน, class คำร้อง KU1 และ KU3 ซึ่งมีการ validate ข้อมูลที่รับมาจาก controller หากไม่ถูกต้องก็จะ โยน error ไปให้หน้า controller จับและแสดง dialog แจ้งข้อผิดพลาดต่อไป
3. ศิริสุข ทานธรรม (ข้าวปั้น)
   * สร้าง models User, UserList และ utilities ในการแปลงข้อมูล Date, String
   * สร้าง TableView component เพื่อคุมตารางภายในหน้าที่ใช้งาน
   * สร้าง TextFieldStack component เพื่อใช้ในการแก้ไขข้อมูล
   * อัปเดทหน้า NisitManagement ให้สามารถแก้ไข Student ด้วยจัดการข้อมูลกับตารางและหน้าแก้ไข 
4. นรากร ธนาพรภักดี (น้ำ)
   * เสนอ และลองทำการโหลดหน้าย่อย แยกกับ tab bar แทนที่จะเป็นการเปลี่ยนทั้งหน้าเมื่อกดไปหน้าอื่น ช่วยลดเวลาในการโหลด และเพื่อให้ tab bar ที่ต้องใช้ทุกหน้าอยู่แล้ว สามารถเขียนและแก้ไข เพียงครั้งเดียว นำไปใช้ได้
   * ทำให้ text field หน้า login และ register สามารถกด tab เพื่อไป text field ต่อไปได้ และทำให้สามารถกด enter แทนการเลื่อน cursor ไปกด ช่วยอำนวยความสะดวก
   * สร้าง user datasource เพื่อให้สามารถอ่าน เขียนข้อมูลลง file csv ได้แทนการ hard code รวมถึงสร้าง file csv และ admin user เมื่อทำการ launch ครั้งแรก
   * สร้าง image datasource สำหรับการอ่านไฟล์รูปภาพ และอัปโหลดไฟล์รูปภาพ ที่ทำงานร่วมกับ component image ของเพื่อนร่วมทีมได้
   * สร้างหน้า เกี่ยวกับพวกเรา ที่มีข้อมูลสมาชิกในทีม
   * สร้าง CircleImage component ซึ่ง derived มาจาก component ของเพื่อนในทีม

### ความก้าวหน้าของระบบครั้งที่ 3
1. ธนอนันท์ เฉลิมพันธ์ (เอ็ม)
   * จัดการระบบในส่วนของ authentication มีการบันทึก timestamp หลังจากที่ทำการเข้าสู่ระบบทุกครั้ง และเพิ่มเงื่อนไขในการตรวจสอบระบบลงทะเบียนร่วมด้วยกับ email ของทางมหาวิทยาลัย
     * เพิ่มระบบในการเปลี่ยนรหัสผ่านครั้งแรกหลังจากที่ อาจารย์ที่ปรึกษา เจ้าหน้าที่ภาค และเจ้าหน้าที่คณะทำการเข้าสู่ระบบในครั้งแรก
   * สร้างในส่วนของโครงสร้างในส่วนของระบบอาจารย์ที่ปรึกษา โดยสามารถเชื่อมกับ datasource รวมทั้งกดเข้าไปดูในส่วน requests เบื้องต้น
   * สร้างในส่วนของระบบเจ้าหน้าที่คณะ 
     * ทำการเพิ่มข้อมูลผู้อนุมัติ แก้ไข และลบได้ (ร่วมกับระบบของเจ้าหน้าที่ภาควิชา)
     * แสดงคำร้องที่ได้เบื้องต้นจากคณะนั้น ๆ โดยเบื้องต้นได้
     * เพิ่มระบบ searchbar ทำให้สามารถค้นหาข้อมูลได้จาก table 
2. รักษิต รุ่งรัตนไชย (เนสท์)
   * ทำหน้า admin จนเสร็จสมบูรณ์ จากการรายงานความคืบหน้าครั้งแรกผมออกแบบหน้า GUI ของ admin ไว้ ในครั้งนี้ผมทำต่อจนเสร็จเกือบจะสมบูรณ์ โดยชี้แจงได้ดังนี้
     * หน้าจัดการข้อมูลผู้ใช้ เสร็จสมบูรณ์ สามารถ ดูรายละเอียดของผู้ใช้ได้ตาม requirement กดให้ใช้งานหรือระงับการใช้งานได้ แสดงรูปภาพได้
     * หน้าจัดการข้อมูลคณะหรือภาควิชา เสร็จสมบูรณ์ ดูรายละเอียดคณะและภาควิชาได้ตาม requirement เพิ่ม หรือ แก้ไขข้อมูลได้
     * หน้าจัดการเจ้าหน้าที่และอาจารย์ที่ปรึกษา เสร็จสมบูรณ์ ดูรายละเอียดของเจ้าหน้าที่และอาจารย์ที่ปรึกษาได้ตาม requirement เพิ่มหรือแก้ไขข้อมูลได้
     * หน้า Dashboard ยังไม่เสร็จสมบูรณ์แต่ออกแบบหน้า GUI กับแสดงข้อมูลบางส่วนได้แล้ว
   * ทำหน้าอาจารย์ที่ปรึกษาเพิ่มเติมจากสิ่งที่เพื่อนทำไว้ ในส่วน จัดการคำร้องที่รอการอนุมัติ ผมเพิ่มฟีเจอร์ การอนุมัติและไม่อนุมัติเข้าไปในแต่ละรายการคำร้อง และ ในส่วนจัดการนิสิตในที่ปรึกษา ผมเพิ่มฟีเจอร์
     การดูรายละเอียดของแต่ละคำร้องของนิสิตในอาจารย์ที่ปรึกษาได้
   * ทำหน้านิสิต ทำในส่วนของการแสดงข้อมูลรายละเอียดของคำร้องเมื่อนิสิตกดสร้างและส่งใบคำร้องแล้ว และสามารถกดดูเหตุผลที่ปฏิเสธคำร้องได้
   * จัดการเรื่อง sort และ search ของทั้ง 3 หน้าที่กล่าวมา
3. ศิริสุข ทานธรรม (ข้าวปั้น)
   * สร้าง approver และ approverList คลาสเพื่อใช้กับ request คลาสและการจัดการคำร้อง
   * อัปเดทการเพิ่มนิสิตด้วย CSV และแจ้ง error ข้อมูลไม่ตรง format, ข้อมูลว่าง และผิดที่บรรทัดไหน
   * restructure user คลาส และแก้ไขให้เก็บข้อมูล faculty และ department ด้วย UUID โดยยังมี backward compatibility
   * สร้าง popup component เพื่อใช้ในโปรเจกต์ และการสร้าง component เช่น confirm popup และ blank popup ที่มี design pattern ให้สามารถเอาไป implement ต่อได้ง่าย
   * implement design pattern กับคลาส user เขียน comparator และ improve search method ด้วย binary search algorithm
   * สร้าง defaultComboBox component ให้สามารถนำไปใช้ได้โดยมีการใช้ design pattern กับ StringExtractor interface
   * สร้างระบบจัดการอาจารย์ที่ปรึกษาให้กับนิสิตภายใต้ภาควิชา
   * สร้างระบบจัดการคำร้องภายในภาควิชา
   * ลองนำเนื้อหาที่เรียนเรื่อง thread มาใช้งานเพื่อเพิ่มประสิทธิภาพให้การโหลดข้อมูลในหน้าจัดการนิสิตของภาควิชา
4. นรากร ธนาพรภักดี (น้ำ)
   * สร้าง Department, Faculty, DepartmentList, และ FacultyList สำหรับจัดการกับ คณะ และ ภาควิชา
   * สร้าง datasource สำหรับ Request, Approver, Department, และ Faculty
   * ทำหน้า StudentRequestInfo ไว้สำหรับโชว์ progress และเวลาที่เกี่ยวข้อง เช่นอัปเดทเป็นสถานะนี้ตอนเวลาไหน
   * แก้ไขหน้า Request ทั้งหมดของ Student ให้โชว์ข้อมูลจาก datasource จริง
   * แก้ไข TableView ในหน้า Student ให้โชว์ข้อมูลใกล้เคียงกับที่ design ไว้ตอนแรก 
   * สร้าง class RequestStatusColumn ไว้สำหรับ style และตั้งค่า column TableView ให้คล้ายกับ design แทนการเขียน code ซ้ำ ๆ ในทุก controller
   * ทำหน้า UserProfileCard สำหรับใช้กับทุก role โดยแสดงข้อมูลตาม role ของผู้ใช้
   * ทำ ParentController Interface ไว้สำหรับหน้าที่ต้องมีการ load หน้าอื่นมาร่วมกับตัวเอง เช่น tab bar เรียกหน้า request ขึ้นมาโชว์
   * ทำ UserCSVReader สำหรับอ่าน file csv เพื่อนำไปใช้ในการเพิ่มนิสิตหลาย ๆ คน โดยไม่ต้องกดเพิ่มทีละคน

### โครงงานที่สมบูรณ์
1. คนที่หนึ่ง ซึ่งยังไม่ใช่ (วัน)
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
2. คนที่สอง แค่ลองรักกัน (ทรู)
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
3. คนที่สาม ต้องห้ามตัวเอง (ทรี)
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
4. คนที่สี่ คนนี้แค่พี่น้อง (โฟร์)
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย
   * อธิบายสิ่งที่เป็นประโยชน์ต่อระบบ และทำให้ทีมภูมิใจ ไม่จำกัดจำนวนข้อย่อย

## วิธีการติดตั้งและรันโปรแกรม
อธิบายวิธีการติดตั้ง และวิธีการรันโปรแกรม รวมถึงที่อยู่ของไฟล์ pdf

## ตัวอย่างข้อมูลผู้ใช้ระบบ (username, password)

## การวางโครงสร้างไฟล์ของโครงงาน