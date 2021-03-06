
TARGET = Projektdesign

# spelling options
ASPELLLANG   := --lang=de
ASPELLPWS    := -p aspell-WordList.pws
ASPELLPREPL  := --repl aspell--ReplaceList.prepl
ASPELLOPT    := --add-tex-command="hypersetup p,lstset p,fancyhead oo,bibliographystyle p,PassOptionsToPackage pp,pdfinfo p,todo p"

# installing options (getting document name from latex files)
INSTALL	:= .
VERSION := $(shell cat $(TARGET).tex | grep \\myVersion | sed 's/.*{\(.*\)}.*/\1/')
TITLE	:= $(shell cat $(TARGET).tex | grep \\myTitle | sed 's/.*{\(.*\)}.*/\1/' | sed 's/ /_/g')

TEXFILES := $(wildcard *.tex)


AUTOIMAGEDIRS := tgif dia octave dot

ifeq ($(V),1)
  Q     =
  QECHO = @\#
else
  Q     = @
  QECHO = @echo
endif

all: auto-images $(TARGET).pdf

new:
	rm -f $(TARGET).pdf

install: all
	cp $(TARGET).pdf $(INSTALL)/$(TITLE)-v$(VERSION).pdf

auto-images:
	$(Q) for i in $(AUTOIMAGEDIRS); do \
	       if [ -d $$i ]; then \
	         $(MAKE) -C $$i; \
	       fi; \
	     done

# PDF
${TARGET}.pdf: $(TEXFILES)
	echo "Running latex..."
	pdflatex ${TARGET}.tex
	#echo "Running bibtex..."
	#bibtex ${TARGET}
	#echo "Rerunning latex...."
	#pdflatex ${TARGET}.tex
	latex_count=5 ; \
	while egrep -s 'Rerun (LaTeX|to get cross-references right)' ${TARGET}.log && [ $$latex_count -gt 0 ] ;\
	    do \
	      echo "Rerunning latex...." ;\
	      pdflatex ${TARGET}.tex ;\
	      latex_count=`expr $$latex_count - 1` ;\
	    done

# spell checker
spell:  $(subst .tex,.tex.bak,$(wildcard *.tex))

# spell checker rule
%.tex.bak: %.tex
	aspell --mode=tex $(ASPELLLANG) $(ASPELLPWS) $(ASPELLPREPL) $(ASPELLOPT) check $<

# garbage collectors
.PHONY:	clean cleanimages distclean mrproper
clean:
	$(QECHO) -n "Cleaning up. "
	$(Q) rm -f *~ *.tex.bak *.aux *.log *.out *.blg *.bbl *.brf *.toc *.lof *.lot *.dvi report.pdf
	$(QECHO) "[ok]"

cleanimages:
	$(Q) for i in $(AUTOIMAGEDIRS); do \
	       if [ -d $$i ]; then \
	         $(MAKE) -C $$i clean; \
	       fi \
	     done

distclean:	clean cleanimages

mrproper: distclean
	rm -f $(TARGET).pdf
