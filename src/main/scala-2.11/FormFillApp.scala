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
      acroForm.getField("name").setValue(order.name)
      acroForm.getField("surname").setValue(order.surname)
      acroForm.getField("dob").setValue(order.dob)
      acroForm.getField("date").setValue(order.date)

      doc.save(new File("order-form-" + order.id +".pdf"))
    }

    doc.close()
  }


  def readCsvFile(file: String): List[Order] ={
    val csvFile = io.Source.fromFile(file)
    val listOfOrders= ListBuffer.empty[Order]

    for (row <- csvFile.getLines()){
      val Array(id, name, surname, dob, date) = row.split(",").map(_.trim)
      listOfOrders.append(new Order(id, name, surname, dob, date))
    }

    listOfOrders.toList
  }
}
