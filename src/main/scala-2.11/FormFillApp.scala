import java.io.File
import org.apache.pdfbox.pdmodel.{PDDocument}

import scala.collection.mutable.ListBuffer

object FormFillApp extends App {

  csvToPdf("./src/test/resources/orders.csv", "./src/test/resources/empty-form.pdf")

  def csvToPdf(csvFile:String, pdfForm:String): Unit ={
    val orders = readCsvFile(csvFile)

    val file = new File(pdfForm)
    val doc = PDDocument.load(file)
    val acroForm = doc.getDocumentCatalog.getAcroForm

    for (order <- orders) {
      for (orderField <- order.getClass.getDeclaredFields){
        val fieldName = orderField.getName
        val value = order.getClass.getMethod(fieldName).invoke(order).asInstanceOf[String]

        acroForm.getField(fieldName).setValue(value)
      }

      doc.save(new File("order-form-" + order.name +".pdf"))
    }

    doc.close()
  }


  def readCsvFile(file: String): List[Order] ={
    val csvFile = io.Source.fromFile(file)
    val listOfOrders= ListBuffer.empty[Order]

    for (row <- csvFile.getLines()){
      val Array(name, surname, dob, date) = row.split(",").map(_.trim)
      listOfOrders.append(new Order(name, surname, dob, date))
    }

    listOfOrders.toList
  }
}
