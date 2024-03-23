package http.validation

import cats.data.ValidatedNec
import cats.syntax.all._
import http.response.ResponseMeeting
import store.Location
import store.StorageMeeting

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import scala.util.Failure
import scala.util.Success
import scala.util.Try

type ValidationResult[A] = ValidatedNec[MeetingRequestValidation, A]

object MeetingRequestValidator {
  def validateMeetingRequest(
      meeting: ResponseMeeting
  ): ValidationResult[StorageMeeting] =
    (
      validateName(meeting.name),
      validateDate(meeting.time),
      validateLocation(meeting.location),
      meeting.notes.valid
    ).mapN(StorageMeeting.apply)

  private def validateName(name: String): ValidationResult[String] =
    if (name.length() > 50) NameTooLong.invalidNec else name.valid

  private def validateDate(
      date: String
  ): ValidationResult[ZonedDateTime] =
    Try(
      ZonedDateTime.parse(
        date,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
      )
    ) match
      case Failure(exception) =>
        println(exception.toString())
        DateMustMatchFormat.invalidNec
      case Success(value) => value.valid

  private def validateLocation(
      location: String
  ): ValidationResult[Location] = Location(123, 123).valid

}

sealed trait MeetingRequestValidation {
  def errorMessage: String
}

case object DateMustMatchFormat extends MeetingRequestValidation {
  def errorMessage: String = "date must match format of yyyy-MM-dd HH:mm:ss Z"
}

case object LocationMustExist extends MeetingRequestValidation {
  def errorMessage: String = "request location must exist"
}

case object NameTooLong extends MeetingRequestValidation {
  def errorMessage: String = "name cannot exceed 50 characters"
}
