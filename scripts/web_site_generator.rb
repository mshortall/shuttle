require './web_page'
require 'Faker'

class WebSiteGenerator

	@directories = []
	@list = []
	@pages = 0
	@dirs = 0
	@path = ""

	def initialize path, pages, dirs
		@pages = pages.to_i
		@dirs = dirs.to_i
		@list = []
		@path = path
	end

	def run
		gen_dirs
		gen_pages
		gen_index
	end

	def gen_dirs
		if @dirs > 10
			@directories = Faker::Lorem.words 10
		else
			@directories = Faker::Lorem.words @dirs
		end
	
		@directories.each do |dir|
			Dir.mkdir(File.join(@path, dir))
		end	

		@directories.push("")
	end

	def gen_pages
		for i in 1..@pages
			page_name = File.join(@path, @directories[i % (@dirs + 1)].to_s, i.to_s)
			@list.push(page_name) 
		end

		@list.each do |page|
			WebPage.new(page, (rand 10) + 1, @list[(@list.index(page) + 1)..(@list.index(page) + 4)]).generate
		end
	end

	def gen_index
		@page = "<html>\n<head>\n<title>Index</title>\n</head>\n<body>\n<p>Links:</p>"
		@list.each do |page|
			@page += "<p><a href='#{page.gsub("test/", "")}'>#{page}</a></p>\n"
		end
		@page += "</body>\n</html>"
		
		File.open(File.join(@path, "index.html"), File::WRONLY|File::CREAT|File::EXCL) do |index|
			index.write(@page)
		end
	end
end

if ARGV.size < 3
	puts "USAGE: \n
					ruby web_site_generator.rb <top directory> <num of pages> <num of dirs>"
else
	gen = WebSiteGenerator.new ARGV[0], ARGV[1], ARGV[2]
	gen.run
end
