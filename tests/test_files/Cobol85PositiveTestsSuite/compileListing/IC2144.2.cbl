1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314                 Date 06/04/2022  Time 11:56:44   Page     1
0Options in effect:
  NOADATA
    ADV
    AFP(NOVOLATILE)
    QUOTE
    ARCH(8)
    ARITH(COMPAT)
  NOAWO
  NOBLOCK0
    BUFSIZE(4096)
  NOCICS
    CODEPAGE(1140)
  NOCOMPILE(S)
  NOCOPYLOC
  NOCOPYRIGHT
  NOCURRENCY
    DATA(31)
    DBCS
  NODECK
  NODEFINE
  NODIAGTRUNC
    DISPSIGN(COMPAT)
  NODLL
  NODUMP
  NODYNAM
  NOEXIT
  NOEXPORTALL
  NOFASTSRT
    FLAG(I,I)
  NOFLAGSTD
    HGPR(PRESERVE)
  NOINITCHECK
  NOINITIAL
    INLINE
    INTDATE(ANSI)
  NOINVDATA
    LANGUAGE(EN)
    LINECOUNT(60)
  NOLIST
    LP(32)
  NOMAP
    MAXPCF(100000)
  NOMDECK
  NONAME
    NSYMBOL(NATIONAL)
  NONUMBER
  NONUMCHECK
    NUMPROC(NOPFD)
    OBJECT
  NOOFFSET
    OPTIMIZE(0)
    OUTDD(SYSOUT)
  NOPARMCHECK
    PGMNAME(COMPAT)
    QUALIFY(COMPAT)
    RENT
    RMODE(AUTO)
  NORULES
1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314                 Date 06/04/2022  Time 11:56:44   Page     2
0 NOSERVICE
    SEQUENCE
    SOURCE(DEC)
    SPACE(1)
  NOSQL
    SQLCCSID
  NOSQLIMS
  NOSSRANGE
  NOSTGOPT
    SUPPRESS
  NOTERM
  NOTEST(NODWARF,NOSOURCE,NOSEPARATE)
  NOTHREAD
    TRUNC(STD)
    TUNE(8)
  NOVBREF
    VLR(STANDARD)
    VSAMOPENFS(COMPAT)
  NOWORD
    XMLPARSE(XMLSS)
    XREF(FULL)
    ZWB
1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314       IC214A    Date 06/04/2022  Time 11:56:44   Page     3
   LineID  PL SL  ----+-*A-1-B--+----2----+----3----+----4----+----5----+----6----+----7-|--+----8 Map and Cross Reference
0  000001         000100 IDENTIFICATION DIVISION.                                         IC2144.2
   000002         000200 PROGRAM-ID.                                                      IC2144.2
   000003         000300     IC214A.                                                      IC2144.2
   000004         000500*                                                              *  IC2144.2
   000005         000600*    VALIDATION FOR:-                                          *  IC2144.2
   000006         000700*                                                              *  IC2144.2
   000007         000800*    "ON-SITE VALIDATION, NATIONAL INSTITUTE OF STD & TECH.     ".IC2144.2
   000008         000900*                                                              *  IC2144.2
   000009         001000*    "COBOL 85 VERSION 4.2, Apr  1993 SSVG                      ".IC2144.2
   000010         001100*                                                              *  IC2144.2
   000011         001300*                                                              *  IC2144.2
   000012         001400*      X-CARDS USED BY THIS PROGRAM ARE :-                     *  IC2144.2
   000013         001500*                                                              *  IC2144.2
   000014         001600*        X-55  - SYSTEM PRINTER NAME.                          *  IC2144.2
   000015         001700*        X-82  - SOURCE COMPUTER NAME.                         *  IC2144.2
   000016         001800*        X-83  - OBJECT COMPUTER NAME.                         *  IC2144.2
   000017         001900*                                                              *  IC2144.2
   000018         002100*    THIS IS SUBPROGRAM IC214.                                    IC2144.2
   000019         002300 ENVIRONMENT DIVISION.                                            IC2144.2
   000020         002400 CONFIGURATION SECTION.                                           IC2144.2
   000021         002500 SOURCE-COMPUTER.                                                 IC2144.2
   000022         002600     XXXXX082.                                                    IC2144.2
   000023         002700 OBJECT-COMPUTER.                                                 IC2144.2
   000024         002800     XXXXX083.                                                    IC2144.2
   000025         002900 DATA DIVISION.                                                   IC2144.2
   000026         003000 LINKAGE SECTION.                                                 IC2144.2
   000027         003100 01  DN1                         PICTURE S9.                      IC2144.2
   000028         003200 PROCEDURE DIVISION USING DN1.                                    IC2144.2 27
   000029         003300 CALL-TEST-1.                                                     IC2144.2
   000030         003400     MOVE 1 TO DN1.                                               IC2144.2 27
   000031         003500 IC214-EXIT.                                                      IC2144.2
   000032         003600     EXIT PROGRAM.                                                IC2144.2
1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314       IC214A    Date 06/04/2022  Time 11:56:44   Page     4
0An "M" preceding a data-name reference indicates that the data-name is modified by this reference.

  Defined   Cross-reference of data names   References

       27   DN1. . . . . . . . . . . . . .  28 M30
1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314       IC214A    Date 06/04/2022  Time 11:56:44   Page     5
0Context usage is indicated by the letter preceding a procedure-name reference.
 These letters and their meanings are:
     A = ALTER (procedure-name)
     D = GO TO (procedure-name) DEPENDING ON
     E = End of range of (PERFORM) through (procedure-name)
     G = GO TO (procedure-name)
     P = PERFORM (procedure-name)
     T = (ALTER) TO PROCEED TO (procedure-name)
     U = USE FOR DEBUGGING (procedure-name)

  Defined   Cross-reference of procedures   References

       29   CALL-TEST-1
       31   IC214-EXIT
1PP 5655-EC6 IBM Enterprise COBOL for z/OS  6.3.0 P220314       IC214A    Date 06/04/2022  Time 11:56:44   Page     6
0 Defined   Cross-reference of programs     References

        3   IC214A
-* Statistics for COBOL program IC214A:
 *    Source records = 32
 *    Data Division statements = 1
 *    Procedure Division statements = 2
 *    Generated COBOL statements = 0
 *    Program complexity factor = 2
0End of compilation 1,  program IC214A,  no statements flagged.
0Return code 0
