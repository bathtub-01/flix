package ca.uwaterloo.flix.language.ast

object Name {

  /**
    * Identifier.
    *
    * @param sp1  the position of the first character in the identifier.
    * @param name the identifier string.
    * @param sp2  the position of the last character in the identifier.
    */
  case class Ident(sp1: SourcePosition, name: String, sp2: SourcePosition) {
    /**
      * The source location of the identifier.
      */
    def loc: SourceLocation = SourceLocation.mk(sp1, sp2)

    /**
      * Human readable representation.
      */
    override def toString: String = name
  }

  /**
    * Namespace.
    *
    * @param sp1    the position of the first character in the namespace.
    * @param idents the identifiers of the namespace.
    * @param sp2    the position of the last character in the namespace.
    */
  case class NName(sp1: SourcePosition, idents: List[Ident], sp2: SourcePosition) {
    /**
      * Returns `true` if this is the root namespace.
      */
    def isRoot: Boolean = idents.isEmpty

    /**
      * The source location of the namespace.
      */
    def loc: SourceLocation = SourceLocation.mk(sp1, sp2)

    /**
      * Human readable representation.
      */
    override def toString: String = idents.mkString(".")
  }

  /**
    * Qualified Name.
    *
    * @param sp1       the position of the first character in the qualified name.
    * @param namespace the namespace
    * @param ident     the identifier.
    * @param sp2       the position of the last character in the qualified name.
    */
  case class QName(sp1: SourcePosition, namespace: NName, ident: Ident, sp2: SourcePosition) {
    /**
      * Returns `true` if this name is qualified by a namespace.
      */
    def isQualified: Boolean = !namespace.isRoot

    /**
      * Returns `true` if this name is unqualified (i.e. has no namespace).
      */
    def isUnqualified: Boolean = !isQualified

    /**
      * The source location of the name.
      */
    def loc: SourceLocation = SourceLocation.mk(sp1, sp2)

    /**
      * Human readable representation.
      */
    override def toString: String =
      if (isUnqualified) ident.toString else namespace.toString + "/" + ident
  }

}
