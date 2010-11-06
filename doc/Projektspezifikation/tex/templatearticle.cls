\NeedsTeXFormat {LaTeX2e}
\ProvidesClass {templatearticle}[2008/03/20 templatearticle for documentation]
\LoadClass [10pt, a4paper, oneside, fleqn]{article}

\RequirePackage{template}

% ------------------------------------------------------------------------------
% Settings only for tracktoolarticle
% ------------------------------------------------------------------------------
% set section numbering to 4 levels
\setcounter{secnumdepth}{4}
\setcounter{tocdepth}{4}

% redefine \paragraph
\def\paragraph{\@startsection{paragraph}{4}{\z@}{3.25ex plus 1ex minus .2ex}{1.5ex plus .2ex}{\normalsize\bf}}

% vspace between paragraphs
\setlength{\parskip}\medskipamount

% set paragraph indention
\parindent=0mm

% ------------------------------------------------------------------------------
% Titelpage Pagestyle (fancy headers)
% ------------------------------------------------------------------------------
\fancypagestyle{frontpage}
{
	\fancyhf{} % clear all header and footer fields
	%\addtolength{\headheight}{0mm}

	\renewcommand{\headrulewidth}{0pt}
	% No spaces or linebreakes in the color box, it will mess up the layout
	\lhead{\textcolor{black}{\Huge{\textbf{\PrintmyProduct}}}}
	\chead{}
	\rhead{\headerlogotitle}

	\renewcommand{\footrulewidth}{0.2pt}
	\lfoot{\PrintmyCompany, \PrintmyStreet, \PrintmyPLZ { }\PrintmyTown\\Tel: \PrintmyPhone, Fax: \PrintmyFax}
	\cfoot{}
	\rfoot{\url{http://\PrintmyWebsite}}
}

% ------------------------------------------------------------------------------
% Title Page definition
% ------------------------------------------------------------------------------
\newcommand{\printTitlePage}[1]
{
	% call \pdfinfo after user settings to provide info in PDF file
	\setPDFinfo

	\begin{titlepage}
	\thispagestyle{frontpage}

	% Wasserzeichen: Vertraulich, Entwurf
	\ifthenelse{\equal{\PrintmyConfidential}{}}
	{
		\ifthenelse{\equal{\PrintmyDraft}{}}
		{}
		{
			\thiswatermark
			{
				\begin{picture}(0, 0)
				\put(200, -250){\includegraphics{draft}}
				\end{picture}
			}
		}
	}
	{
		\ifthenelse{\equal{\PrintmyDraft}{}}
		{
			\thiswatermark
			{
				\begin{picture}(0, 0)
				\put(200, -250){\includegraphics{confidential}}
				\end{picture}
			}
		}
		{
			\thiswatermark
			{
				\begin{picture}(0, 0)
				\put(200, -185){\includegraphics{draft}}
				\put(200, -350){\includegraphics{confidential}}
				\end{picture}
			}
		}
	}

	\vspace*{5mm} % needs the * for a vspace here

	\frame{
		\begin{tabular*}{\textwidth}[b]{l}
		\\[-0.9ex]
		\Huge{\textbf{\PrintmyDoctype}}\\[0.5ex]
		\huge{\textbf{\PrintmyDivision}}\\[10cm]
		\huge{\textbf{\PrintmyTitle}}\\[0.5ex]
		\Large{\PrintmySubTitle}\\[0.2ex]
		\end{tabular*}
	}

	\vspace{5mm}

	\begin{tabular*}{\textwidth}[b]{l@{\extracolsep{\fill}} r}
		
		Version \PrintmyVersion & \PrintmyAuthor \\
		\@date \\ %Dokument \PrintmyDocnum \\[2ex]
		\Large{\sc{\textcolor{red}\PrintmyConfidential}} \\
	\end{tabular*}

	\begin{center}
		\parbox[c]{0.8\textwidth}{\setlength{\parskip}\smallskipamount \Large{\ifthenelse{\equal{\PrintmyLanguage}{english}}{Abstract}{Zusammenfassung}}\\[0.5ex]\small{\input{#1}}}
	\end{center}

	\end{titlepage}
}

% ------------------------------------------------------------------------------
% leader Pages definition
% ------------------------------------------------------------------------------
\newcommand{\printLeader}[1]
{
	\pagenumbering{roman}
	\input{#1}
	\tableofcontents
	\newpage
	\pagenumbering{arabic}
}
