require 'Faker'

class WebPage

	def initialize file, paras, links 
		@file = file
		@paras = paras
		@links = links
		@page = ""
	end

	def generate
		page_start
		head_start
		add_title
		head_end
		body_start
		while (@links.size > 0 || @paras >= 0)
			if (rand 100) % 2 == 0 && @links.size > 0
				add_link @links.pop
			elsif @paras >= 0
				add_paragraph (rand 10) + 1
				@paras -= 1
			end
		end
		body_end
		page_end
		write
	end
	
	def write
		File.open(@file, File::WRONLY|File::CREAT|File::EXCL) do |file|
			file.write(@page)
		end
	end
	
	
	def page_start
		@page += "<html>\n"
	end

	def page_end 
		@page +="</html>"
	end

	def add_link link_to
		@page += "<p><a href='/#{link_to}'>#{link_to}</a></p>\n"
	end

	def add_paragraph size
		@page += "<p>#{Faker::Lorem.paragraph(size)}</p>\n"
	end

	def add_title
		@page += "<title>#{Faker::Company.catch_phrase}</title>"
	end

	def head_start
		@page += "<head>\n"
	end

	def head_end
		@page += "</head>\n"
	end

	def body_start
		@page += "<body>\n"
	end

	def body_end
		@page += "</body>\n"
	end

end
